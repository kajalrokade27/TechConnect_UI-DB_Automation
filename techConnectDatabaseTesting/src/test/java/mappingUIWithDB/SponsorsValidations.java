package mappingUIWithDB;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.TechConnect.Base.AdminBaseClass;
import com.TechConnect.FileUtility.DBUtility;
import com.TechConnect.FileUtility.GetPropertyData;
import com.tc.AdminPOM.SponsorsPage;
import com.tech_Connect.Action.ActionClass;

public class SponsorsValidations extends AdminBaseClass
{
	 @Test
	   public void sponsorValidationTest() throws InterruptedException, AWTException, IOException, SQLException
	   {
		
		  SponsorsPage sp = new SponsorsPage(driver);
		   ActionClass.click(sp.sponsors_link);
		   ActionClass.click(sp.addSponsorButton);
		   Thread.sleep(2000); 
		   ActionClass.enterText(sp.sponsor_fields.get(0), GetPropertyData.propData("sponsor_name"));
		   ActionClass.enterText(sp.sponsor_fields.get(1), GetPropertyData.propData("spons_url"));
		   
		   ActionClass.click(sp.sponsor_logo);
		   ActionClass.uploadFile(GetPropertyData.propData("company_logo"));
		   ActionClass.click(sp.banner_image);
		   ActionClass.uploadFile(GetPropertyData.propData("banner_image"));
		   
		  ActionClass.selectByVisibleText(sp.spons_drop.get(0), "Gold");
		  ActionClass.selectByVisibleText(sp.spons_drop.get(1), "General");
		  
		   ActionClass.enterText(sp.desc_field, GetPropertyData.propData("spons_desc"));
		   ActionClass.click(sp.submitButton);
		   ActionClass.waitUptoVisible(sp.success_msg);
		   Assert.assertTrue(sp.success_msg.isDisplayed(), "Success message is not displayed after adding sponsor");
		   Reporter.log("Sponsor created successfully.", true);
		   
		   //Verify sponsor is added in the database

	        // Verify sponsor is added in the database
	        DBUtility db = new DBUtility();
	        db.connectToDB(GetPropertyData.propData("db_url"), GetPropertyData.propData("db_user"), GetPropertyData.propData("db_password"));
	        String sponsorName = GetPropertyData.propData("sponsor_name");
	        boolean sponsorExists = db.isSponsorPresent(sponsorName);
	        db.closeConn();
	        Assert.assertTrue(sponsorExists, "Sponsor not found in database");
	        Reporter.log("Sponsor "+ sponsorName+" verified in database.", true); 
	        
		  }
	 
	 @Test
	 public void allSponsorsValidationTest() throws IOException, SQLException 
	 {
		 SponsorsPage sp = new SponsorsPage(driver);
		    ActionClass.click(sp.sponsors_link);
		    

		    DBUtility db = new DBUtility();
		    db.connectToDB(GetPropertyData.propData("db_url"), GetPropertyData.propData("db_user"), GetPropertyData.propData("db_password"));

		    for (WebElement sponsorElement : sp.sponsors_list) {
		        String sponsorName = sponsorElement.getText().trim();
		        boolean sponsorExists = db.isSponsorPresent(sponsorName);
		        if (sponsorExists) 
		        {
		            Reporter.log("Sponsor " + sponsorName + " is present in database.", true);
		        } 
		        else
		        {
		            Reporter.log("Sponsor " + sponsorName + " is NOT present in database.", true);
		        }
		    }

	 }
}

