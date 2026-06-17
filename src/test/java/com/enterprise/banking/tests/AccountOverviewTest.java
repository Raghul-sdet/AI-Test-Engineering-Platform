package com.enterprise.banking.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enterprise.banking.pages.AccountOverviewPage;
import com.enterprise.banking.pages.LoginPage;

public class AccountOverviewTest extends BaseTest {

    // CRITICAL UPDATE: The method signature now perfectly matches the 6 columns in TestData.xlsx
    @Test(dataProvider = "Login data", dataProviderClass = com.enterprise.banking.utils.ExcelUtils.class)
    public void verifyAccountOverviewAndBalance(String username, String password, String transferAmount, 
                                                String searchAmount, String searchDate, String searchTransId) {
        
        System.out.println(">>> Executing Account Overview Test for user: " + username);

        // Step 1: Login (Parabank automatically redirects to Overview upon success)
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        // Step 2: Initialize Overview Page 
        AccountOverviewPage overviewPage = new AccountOverviewPage(driver);

        // Step 3: Validate Balance
        String balance = overviewPage.getAccountBalance();
        System.out.println(">>> Extracted Account Balance for user '" + username + "' is: " + balance);
        Assert.assertTrue(balance.contains("$"), "Assertion Failed: Balance missing currency formatting.");

        // Step 4: Logout
        overviewPage.clickLogout();
    }
}