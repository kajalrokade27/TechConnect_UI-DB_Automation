package mappingUIWithDB;

import java.io.IOException;
import java.sql.SQLException;

import org.testng.Reporter;
import org.testng.annotations.Test;

import com.TechConnect.Base.AdminBaseClass;
import com.TechConnect.FileUtility.DBUtility;
import com.TechConnect.FileUtility.GetPropertyData;
import com.tc.AdminPOM.ConferencePage;
import com.tech_Connect.Action.ActionClass;


public class ConferenceAttendeeValidation extends AdminBaseClass
{
   @Test
   public void conferenceAttendeeValidationTest() throws SQLException, IOException 
   {
	    ConferencePage cp = new ConferencePage(driver);
	    ActionClass.click(cp.EventDropdown);
	    ActionClass.click(cp.conferenceLink);
	    ActionClass.click(cp.filter_Cards.get(3));
	    String eventName = cp.eventCardName.getText().trim();// Assuming the first card is the one to be clicked
	    ActionClass.click(cp.eventCardName);
	    ActionClass.click(cp.attendeesTab);
	    
	           DBUtility db = new DBUtility();
	           db.connectToDB(
	           GetPropertyData.propData("db_url"),
	           GetPropertyData.propData("db_user"),
	           GetPropertyData.propData("db_password")
	       );

	       int uiAttendeeCount = cp.attendeeEmail.size();
	       int dbAttendeeCount = db.getAttendeeCountForEvent(eventName);

	       Reporter.log("UI Attendee Count for Event "+ eventName+" : " + uiAttendeeCount, true);
	       Reporter.log("DB Attendee Count for Event "+ eventName+" : " + dbAttendeeCount, true);

	       for (int i = 0; i < cp.attendeeEmail.size(); i++) 
	       {
	           String attendeeEmail = cp.attendeeEmail.get(i).getText().trim();
	           String attendeeName = cp.attendeeNames.get(i).getText().trim();
	           boolean exists = db.isAttendeePresent(attendeeEmail, eventName);
	           if (exists) 
	           {
	        	   Reporter.log("Attendee " + attendeeName + " (" + attendeeEmail + ") registered for event '" + eventName + "' is present in the database.", true);
	           } 
	           else 
	           {
	        	   Reporter.log("Attendee " + attendeeName + " (" + attendeeEmail + ") registered for event '" + eventName + "' is NOT present in the database.", true);
	           }
	       }

	       db.closeConn();
	   }
	}

	    
	    
