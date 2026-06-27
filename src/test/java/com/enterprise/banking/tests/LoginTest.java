package com.enterprise.banking.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enterprise.banking.pages.LoginPage;

public class LoginTest extends BaseTest {

    // Route directly to the new H2 Database Provider
    @Test(dataProvider = "h2-login-data", dataProviderClass = com.enterprise.banking.utils.H2DataProvider.class)
    public void testBankLogin(String username, String password, String transferAmount, 
                              String searchAmount, String searchDate, String searchTransId) {
        
        System.out.println(">>> Executing Login Test for user: " + username);
        
        // Ensure thread safety using getDriver()
        LoginPage loginPage = new LoginPage(getDriver());
        
        // Execute login
        loginPage.login(username, password);
        
        // Assertion for valid user credentials based on H2 dynamic data
        boolean isDashboardDisplayed = loginPage.isOverviewDisplayed();
        Assert.assertTrue(isDashboardDisplayed, "H2 Database user login failed for: " + username);
        
        // Clean Logout
        loginPage.logout();
    }
}