package com.enterprise.banking.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enterprise.banking.pages.RegistrationPage;
import com.enterprise.banking.utils.ExcelWriteUtils;
import com.enterprise.banking.utils.RandomDataGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegistrationTest extends BaseTest {

    @Test
    public void verifyUserRegistrationAndDataStorage() {
        
        // Step 1: Generate dynamic test data
        String newUsername = RandomDataGenerator.generateRandomUsername();
        String newPassword = RandomDataGenerator.generateRandomPassword();

        System.out.println(">>> Attempting registration with Username: " + newUsername);

        // Step 2: Initialize Page Object
        RegistrationPage registrationPage = new RegistrationPage(driver);

        // Step 3: Execute Registration UI Workflow
        registrationPage.navigateToRegistration();
        registrationPage.fillRegistrationForm(newUsername, newPassword);
        registrationPage.clickSubmitRegistration();

        // Step 4: Strict Validation
        String actualSuccessMessage = registrationPage.getRegistrationSuccessMessage();
        Assert.assertTrue(actualSuccessMessage.contains("Welcome"), 
                "Assertion Failed: Registration success message did not display the expected text.");

        // Step 5: Save verified credentials back to Excel with default test parameters
        String excelPath = "src/test/resources/TestData.xlsx";
        String sheetName = "Login data"; 
        
        String defaultTransferAmount = "50"; 
        String defaultSearchAmount = "50"; // We search for 50 because we transfer 50
        
        // Dynamically get today's date in MM-DD-YYYY format for Parabank
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String todayDate = LocalDate.now().format(formatter);
        
        String emptyTransactionId = ""; // Leave empty; it will skip this specific search in the test
        
        // CRITICAL FIX: Pass all 8 required arguments to the utility
        ExcelWriteUtils.appendCredentialsToExcel(excelPath, sheetName, newUsername, newPassword, 
                defaultTransferAmount, defaultSearchAmount, todayDate, emptyTransactionId);
        
        System.out.println(">>> Execution Complete. Next suite run will automatically test user: " + newUsername);
    }
}