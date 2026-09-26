package com.enterprise.banking.utils;

import com.enterprise.banking.repositories.UserRepository;
import org.testng.annotations.DataProvider;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class H2DataProvider {

    // === The NEW TestNG DataProvider Bridge (H2 JDBC) ===
    @DataProvider(name = "h2-login-data")
    public static Object[][] getDatabaseLoginData() {
        System.out.println("=====================================");
        System.out.println(">>> H2 DATA PROVIDER TRIGGERED <<<");
        System.out.println("Routing TestNG to read from Database instead of Excel...");
        System.out.println("=====================================");
        
        try {
            // FIX: Wrapped the 1D String[] into a 2D Object[][] to satisfy TestNG requirements
            return new Object[][] { 
                UserRepository.getLatestUserForTest() 
            };
        } catch (RuntimeException e) {
            // Fallback/Seed logic
            System.out.println("WARNING: No user found in H2 DB. Seeding default user 'john'.");
            try {
                // Check if john already exists to be idempotent
                boolean exists = false;
                String dbPassword = System.getProperty("DB_PASSWORD", System.getenv().getOrDefault("DB_PASSWORD", "TestDbP@ss123!"));
                try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:h2:file:./target/h2db/test_users_db;AUTO_SERVER=TRUE", "sa", dbPassword);
                     java.sql.PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM test_users WHERE username='john'");
                     java.sql.ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        exists = true;
                    }
                }
                if (!exists) {
                    UserRepository.saveUser("john", "demo");
                }
                return new Object[][] {
                    UserRepository.getLatestUserForTest()
                };
            } catch (Exception ex) {
                System.out.println("WARNING: Failed to seed or read from H2 DB. Using ParaBank default demo credentials.");
                String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                return new Object[][] {
                    new String[] { "john", "demo", "10", "10", todayDate, "12345" }
                };
            }
        }
    }
}