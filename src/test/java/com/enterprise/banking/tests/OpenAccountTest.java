package com.enterprise.banking.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enterprise.banking.pages.LoginPage;
import com.enterprise.banking.pages.OpenAccountPage;

public class OpenAccountTest extends BaseTest {

    // CRITICAL PHASE 3 CUTOVER: Pointing specifically to the new H2DataProvider
    @Test(dataProvider = "h2-login-data", dataProviderClass = com.enterprise.banking.utils.H2DataProvider.class)
    public void verifyOpenNewAccountWorkflow(String username, String password, String transferAmount, 
                                             String searchAmount, String searchDate, String searchTransId) {
        
        System.out.println(">>> Executing Open Account Test for user: " + username);

        // Step 1: Login
        // FIX: Replaced 'driver' with 'getDriver()' for Thread-Safety
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        // Step 2: Initialize Page Object
        // FIX: Replaced 'driver' with 'getDriver()'
        OpenAccountPage openAccountPage = new OpenAccountPage(getDriver());

        // Step 3: Execute Open Account Workflow
        openAccountPage.navigateToOpenNewAccount();
        openAccountPage.selectAccountType("SAVINGS");
        openAccountPage.selectFromAccountByIndex(0);
        openAccountPage.clickSubmit();

        // Step 4: Validate Success and Extract Account ID
        Assert.assertTrue(openAccountPage.isAccountOpenedSuccessfully(), 
                "Assertion Failed: Success header is missing or incorrect!");
        
        String newAccountId = openAccountPage.getGeneratedAccountNumber();
        System.out.println(">>> SUCCESS! Newly Generated Account Number for user '" + username + "' is: " + newAccountId);

        // Step 5: Clean Logout
        openAccountPage.clickLogout();
    }
}