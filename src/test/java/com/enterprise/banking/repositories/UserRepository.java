package com.enterprise.banking.repositories;

import com.enterprise.banking.utils.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserRepository {

    /**
     * Initializes the H2 database schema safely. 
     * Uses 'IF NOT EXISTS' to ensure it doesn't overwrite existing data during parallel runs.
     */
    public static synchronized void initializeSchema() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS test_users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(100) NOT NULL UNIQUE, " +
                "password VARCHAR(100) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
             
            stmt.execute(createTableSql);
            System.out.println(">>> [H2 DB] Schema Initialized: 'test_users' table is ready.");
            
        } catch (SQLException e) {
            throw new RuntimeException("CRITICAL: Failed to initialize H2 database schema.", e);
        }
    }

    /**
     * Saves a newly registered user to the database.
     * try-with-resources automatically closes the Connection and PreparedStatement.
     */
    public static void saveUser(String username, String password) {
        String insertSql = "INSERT INTO test_users (username, password) VALUES (?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(">>> [H2 DB] User saved successfully to Database: " + username);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user to H2 database: " + username, e);
        }
    }

    /**
     * Fetches the most recently registered user from the database.
     * Returns an Object[][] array formatted perfectly for TestNG DataProviders.
     */
    public static Object[][] getLatestUserForTest() {
        // Query to get the 1 most recently created user
        String selectSql = "SELECT username, password FROM test_users ORDER BY id DESC LIMIT 1";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSql)) {
            
            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                
                System.out.println(">>> [H2 DB] Successfully retrieved latest user for testing: " + username);
                
                // Return exactly what TestNG expects: a 2D array.
                // The remaining parameters are mapped here to seamlessly match the legacy method signature.
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                String todayDate = sdf.format(new Date());
                
                return new Object[][] {
                    { username, password, "50", "50", todayDate, "" }
                };
            } else {
                throw new RuntimeException("CRITICAL: H2 Database is empty. No users found to run tests.");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve user from H2 database.", e);
        }
    }
}