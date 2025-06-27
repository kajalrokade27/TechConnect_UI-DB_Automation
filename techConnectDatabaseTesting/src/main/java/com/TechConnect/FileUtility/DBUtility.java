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
    public void closeConn() throws SQLException 
    {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (conn != null) conn.close();
    }
}