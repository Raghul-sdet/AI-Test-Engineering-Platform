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
            // Fallback: If no user exists in H2 (first run or empty DB),
            // use ParaBank's built-in demo credentials to prevent cascade failures
            System.out.println("WARNING: No user found in H2 DB. Using ParaBank default demo credentials.");
            String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
            return new Object[][] {
                new String[] { "john", "demo", "10", "10", todayDate, "12345" }
            };
        }
    }
}