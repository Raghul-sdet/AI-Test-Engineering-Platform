package com.enterprise.banking.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enterprise.banking.pages.LoginPage;
import com.enterprise.banking.pages.OpenAccountPage;

public class OpenAccountTest extends BaseTest {

    // CRITICAL UPDATE: Signature now accepts all 6 DataProvider parameters
    @Test(dataProvider = "Login data", dataProviderClass = com.enterprise.banking.utils.ExcelUtils.class)
    public void verifyOpenNewAccountWorkflow(String username, String password, String transferAmount, 
                                             String searchAmount, String searchDate, String searchTransId) {
        
        System.out.println(">>> Executing Open Account Test for user: " + username);

        // Step 1: Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        // Step 2: Initialize Page Object
        OpenAccountPage openAccountPage = new OpenAccountPage(driver);

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