package com.TechConnect.FileUtility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtility {
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public void connectToDB(String url, String user, String password) throws SQLException 
    {
        conn = DriverManager.getConnection(url, user, password);
    }

   
 // Get active user by email
    public ResultSet getUserByEmail(String email) throws SQLException {
        String query = "SELECT email FROM users WHERE status = 'active' AND email = ?";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, email);
        rs = pstmt.executeQuery();
        return rs;
    }

    // Get users by role and their status
    public ResultSet getUsersByRole(String role) throws SQLException {
        String query = "SELECT full_name, status FROM users WHERE role = ?";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, role);
        rs = pstmt.executeQuery();
        return rs;
    }
   
    //Get user by email and role
    public ResultSet getUserByEmailAndRole(String email, String role) throws SQLException {
		String query = "SELECT full_name, email FROM users WHERE email = ? AND role = ?";
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, email);
		pstmt.setString(2, role);
		rs = pstmt.executeQuery();
		return rs;
	}
    public boolean isSponsorPresent(String sponsorName) throws SQLException {
        String query = "SELECT company_name FROM sponsors WHERE company_name = ?";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, sponsorName);
        rs = pstmt.executeQuery();
        boolean exists = rs.next();
        rs.close();
        pstmt.close();
        return exists;
    }
    public boolean isAttendeePresent(String attendeeEmail, String eventName) throws SQLException {
        String query = "SELECT attendee_email FROM attendees WHERE attendee_email = ? AND event_name = ?";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, attendeeEmail);
        pstmt.setString(2, eventName);
        rs = pstmt.executeQuery();
        boolean exists = rs.next();
        rs.close();
        pstmt.close();
        return exists;
    }

    public int getAttendeeCountForEvent(String eventName) throws SQLException {
        String query = "SELECT COUNT(*) FROM attendees WHERE event_name = ?";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, eventName);
        rs = pstmt.executeQuery();
        int count = 0;
        if (rs.next()) 
        {
            count = rs.getInt(1);
        }
        rs.close();
        pstmt.close();
        return count;
    }

    public void closeConn() throws SQLException 
    {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (conn != null) conn.close();
    }
}