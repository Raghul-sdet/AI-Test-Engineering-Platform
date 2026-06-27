package com.enterprise.banking.tests;

import com.enterprise.banking.utils.DBUtility;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class DatabaseValidationTest {

    @BeforeMethod
    public void setupDbConnection() {
        DBUtility.openConnection();
        
        // Seed test data strictly for this isolated unit test
        DBUtility.executeUpdate("INSERT INTO users (username, password) VALUES (?, ?)", "DBUser01", "Pass123");
        DBUtility.executeUpdate("INSERT INTO accounts (account_id, user_id, balance) VALUES (?, SELECT id FROM users WHERE username = ?, ?)", 
                                99999, "DBUser01", 1500.50);
    }

    @Test
    public void verifyUserBalanceInDatabase() {
        System.out.println(">>> Executing JDBC Database Validation...");

        // Query the shadow backend database
        String sql = "SELECT u.username, a.account_id, a.balance " +
                     "FROM users u JOIN accounts a ON u.id = a.user_id " +
                     "WHERE u.username = ?";
                     
        List<Map<String, Object>> results = DBUtility.executeQuery(sql, "DBUser01");

        // Assertions
        Assert.assertFalse(results.isEmpty(), "DB ASSERTION FAILED: User was not found in the database.");
        
        Map<String, Object> userData = results.get(0);
        
        Assert.assertEquals(userData.get("username").toString(), "DBUser01", "DB Username mismatch");
        Assert.assertEquals(userData.get("account_id").toString(), "99999", "DB Account ID mismatch");
        
        // Convert BigDecimal to String for safe comparison
        Assert.assertEquals(userData.get("balance").toString(), "1500.50", "DB Backend Balance mismatch");
        
        System.out.println(">>> DB Backend Validation Passed for user: DBUser01");
    }

    @AfterMethod
    public void tearDownDbConnection() {
        DBUtility.closeConnection();
    }
}