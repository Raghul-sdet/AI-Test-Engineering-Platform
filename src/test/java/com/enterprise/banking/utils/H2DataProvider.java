package com.enterprise.banking.utils;

import com.enterprise.banking.repositories.UserRepository;
import org.testng.annotations.DataProvider;

public class H2DataProvider {

    // === The NEW TestNG DataProvider Bridge (H2 JDBC) ===
    @DataProvider(name = "h2-login-data")
    public static Object[][] getDatabaseLoginData() {
        System.out.println("=====================================");
        System.out.println(">>> H2 DATA PROVIDER TRIGGERED <<<");
        System.out.println("Routing TestNG to read from Database instead of Excel...");
        System.out.println("=====================================");
        
        return UserRepository.getLatestUserForTest();
    }
}