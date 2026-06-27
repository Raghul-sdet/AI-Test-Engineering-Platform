package com.enterprise.banking.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enterprise.banking.pages.AccountOverviewPage;
import com.enterprise.banking.pages.LoginPage;

public class AccountOverviewTest extends BaseTest {

    // CRITICAL PHASE 2 CUTOVER: Pointing specifically to the new H2DataProvider
    @Test(dataProvider = "h2-login-data", dataProviderClass = com.enterprise.banking.utils.H2DataProvider.class)
    public void verifyAccountOverviewAndBalance(String username, String password, String transferAmount, 
                                                String searchAmount, String searchDate, String searchTransId) {
        
        System.out.println(">>> Executing Account Overview Test for user: " + username);

        // Step 1: Login (Parabank automatically redirects to Overview upon success)
        // FIX: Replaced 'driver' with 'getDriver()' for Thread-Safe Parallel Execution
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        // Step 2: Initialize Overview Page 
        // FIX: Replaced 'driver' with 'getDriver()'
        AccountOverviewPage overviewPage = new AccountOverviewPage(getDriver());

        // Step 3: Validate Balance
        String balance = overviewPage.getAccountBalance();
        System.out.println(">>> Extracted Account Balance for user '" + username + "' is: " + balance);
        Assert.assertTrue(balance.contains("$"), "Assertion Failed: Balance missing currency formatting.");

        // Step 4: Logout
        overviewPage.clickLogout();
    }
}