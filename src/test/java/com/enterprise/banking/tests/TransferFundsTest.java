package com.enterprise.banking.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enterprise.banking.pages.LoginPage;
import com.enterprise.banking.pages.TransferFundsPage;

public class TransferFundsTest extends BaseTest {

    // CRITICAL UPDATE: Signature now accepts all 6 DataProvider parameters
    @Test(dataProvider = "Login data", dataProviderClass = com.enterprise.banking.utils.ExcelUtils.class)
    public void verifyFundTransferWorkflow(String username, String password, String transferAmount, 
                                           String searchAmount, String searchDate, String searchTransId) {
        
        System.out.println(">>> Executing Transfer Funds Test for user: " + username);

        // Step 1: Login with Excel Credentials
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        // Step 2: Initialize Page Object
        TransferFundsPage transferPage = new TransferFundsPage(driver);

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