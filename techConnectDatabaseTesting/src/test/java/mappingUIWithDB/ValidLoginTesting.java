package mappingUIWithDB;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.TechConnect.Base.AdminBaseClass;
import com.TechConnect.FileUtility.DBUtility;
import com.TechConnect.FileUtility.GetPropertyData;
import com.tc.AdminPOM.DashBoardPage;
import com.tech_Connect.Action.ActionClass;

public class ValidLoginTesting extends AdminBaseClass
{
	DBUtility dbUtil ;
   @Test
   public void testValidLogin() throws SQLException, IOException 
   {
       // Landed on dashboard page bcz the login is already performed in the AdminBaseClass
	   DashBoardPage dp = new DashBoardPage(driver);
	   ActionClass.waitUptoClickable(dp.profileButton);
	   ActionClass.click(dp.profileButton);

       // Get the email of the currently logged-in admin from the UI
	   String loggedInAdminEmail = dp.profileEmail.getText();
	   
	  // Initialize DB utility and connect to the database using credentials from properties file
	   dbUtil = new DBUtility();
	   dbUtil.connectToDB(GetPropertyData.propData("db_url"), GetPropertyData.propData("db_user"), GetPropertyData.propData("db_password"));
	   Reporter.log("Successfully connected to database", true);
	   
	// Execute the query to search the user by email and is active status
	   ResultSet rs = dbUtil.getUserByEmail(loggedInAdminEmail);

	// Check if the logged-in admin email exists among active users
	    boolean userFound = false;
	    while (rs.next()) 
	    {
	        String dbEmail = rs.getString("email");
	        if (dbEmail.equals(loggedInAdminEmail))
	        {
	            userFound = true;
	            Reporter.log("Logged in Admin email found in database with active status. Email Found: " + dbEmail, true);
	            break;
	        }
	    }
	    Assert.assertTrue(userFound, "Admin not found in the database with email: " + loggedInAdminEmail);
		 
		 dbUtil.closeConn();
		 Reporter.log("Database connection closed successfully", true);
   }
    

}
