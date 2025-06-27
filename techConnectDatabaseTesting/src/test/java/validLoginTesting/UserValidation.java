package validLoginTesting;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.TechConnect.Base.AdminBaseClass;
import com.TechConnect.FileUtility.DBUtility;
import com.TechConnect.FileUtility.GetPropertyData;
import com.tc.AdminPOM.UsersPage;
import com.tech_Connect.Action.ActionClass;

public class UserValidation extends AdminBaseClass
{
	DBUtility dbUtil = new DBUtility();
	
    // Add a user with valid input and verify it is present in the database
    @Test(priority=1)
    public void addUsersWithValidInput() throws EncryptedDocumentException, IOException, InterruptedException, SQLException
    {
   	 UsersPage up = new UsersPage(driver);
   	 ActionClass.click(up.users);
   	 ActionClass.waitUptoVisible(up.add_users);
   	 ActionClass.click(up.add_users);
   	 ActionClass.enterText(up.u_name, GetPropertyData.propData("u_name"));
   	 ActionClass.enterText(up.u_email,GetPropertyData.propData("u_email"));
   	 ActionClass.enterText(up.u_company, GetPropertyData.propData("u_company"));
   	 ActionClass.enterText(up.u_jobTitle, GetPropertyData.propData("u_jobTitle"));
   	 ActionClass.click(up.submitButton);
    // ActionClass.waitUptoVisible(up.success_msg);
   	// assertTrue(up.success_msg.isDisplayed());
   	 
   	 //Verify user added is present in the database
   	 dbUtil.connectToDB(GetPropertyData.propData("db_url"), GetPropertyData.propData("db_user"), GetPropertyData.propData("db_password"));
   	 ResultSet rs = dbUtil.getUserByEmailAndRole(GetPropertyData.propData("u_email"), "user");
   	 boolean userFound = false;
   	 while (rs.next()) 
   	 {
   	     String dbEmail = rs.getString("email");
   	     String dbRole = rs.getString("role");
   	     if (dbEmail.equals(GetPropertyData.propData("u_email")) && dbRole.equals("user")) 
   	     {
   	         userFound = true;
   	         Reporter.log("User with email " + dbEmail +  " found in database", true);
   	         break;
   	     }
   	 }
   	 Assert.assertTrue(userFound, "User with email " + GetPropertyData.propData("u_email") + " not found in database");
   	 dbUtil.closeConn();
  }
    
    // Validate all users with role 'user' are present in the UI and DB
    @Test(priority=2)
    public void allUsersValidation() throws InterruptedException, SQLException, IOException
	{
    	 UsersPage up = new UsersPage(driver);
      	 ActionClass.click(up.users);
      	 List<WebElement> userElements = up.users_list;
      	 List<String> uiUsers = new ArrayList<>();

        for (WebElement ele : userElements) 
        {
            String name = ele.getText().trim();
            if (!name.isEmpty()) 
            {
                uiUsers.add(name);
            }
        }
        dbUtil.connectToDB(GetPropertyData.propData("db_url"), GetPropertyData.propData("db_user"), GetPropertyData.propData("db_password"));
        
     // 2. Get DB Users with role = 'user'
        Map<String, String> dbUsers = new HashMap<>();
        ResultSet rs = dbUtil.getUsersByRole("user");

        while (rs.next()) 
        {
            dbUsers.put(rs.getString("full_name").trim(), rs.getString("status").trim());
        }

        // 3. Compare UI with DB
        for (String uiUser : uiUsers)
        {
            if (dbUsers.containsKey(uiUser))
            {
                Reporter.log("✅ " + uiUser + " found in DB | Status: " + dbUsers.get(uiUser),true);
            } else
            {
                Reporter.log("❌ " + uiUser + " shown in UI but NOT found in DB with role='user'",true);
                Assert.fail(uiUser + " is missing from DB or does not have role='user'");
            }
        }
    }

}
