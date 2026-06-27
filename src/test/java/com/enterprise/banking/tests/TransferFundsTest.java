package com.enterprise.banking.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enterprise.banking.pages.LoginPage;
import com.enterprise.banking.pages.TransferFundsPage;

public class TransferFundsTest extends BaseTest {

    // CRITICAL PHASE 3 CUTOVER: Pointing specifically to the new H2DataProvider
    @Test(dataProvider = "h2-login-data", dataProviderClass = com.enterprise.banking.utils.H2DataProvider.class)
    public void verifyFundTransferWorkflow(String username, String password, String transferAmount, 
                                           String searchAmount, String searchDate, String searchTransId) {
        
        System.out.println(">>> Executing Transfer Funds Test for user: " + username);

        // Step 1: Login with Database Credentials
        // FIX: Replaced 'driver' with 'getDriver()' for Thread-Safety
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        // Step 2: Initialize Page Object
        // FIX: Replaced 'driver' with 'getDriver()'
        TransferFundsPage transferPage = new TransferFundsPage(getDriver());

        // Step 3: Execute Transfer Workflow
        transferPage.navigateToTransferFunds();
        transferPage.enterTransferAmount(transferAmount);
        
        // Using index 0 ensures compatibility even for new users who only possess one account
        transferPage.selectFromAccountByIndex(0);
        transferPage.selectToAccountByIndex(0);
        
        transferPage.clickTransfer();

        // Step 4: Validate Success Header
        Assert.assertTrue(transferPage.isTransferComplete(), 
                "Assertion Failed: Transfer success header was missing or application threw an internal error.");

        // Step 5: Validate Transaction Details
        String actualAmountDisplayed = transferPage.getTransferredAmountText();
        
        Assert.assertTrue(actualAmountDisplayed.contains("$"), 
                "Assertion Failed: Transferred amount missing currency formatting.");
        
        // Step 6: Clean Logout to prevent state leaks for the next execution
        transferPage.clickLogout();
        
        System.out.println(">>> Transfer Validation Successful for user: " + username);
    }
}