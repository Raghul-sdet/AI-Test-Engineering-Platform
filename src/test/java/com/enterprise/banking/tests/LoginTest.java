package com.enterprise.banking.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.enterprise.banking.pages.LoginPage;
import com.enterprise.banking.utils.ExcelUtils;

public class LoginTest extends BaseTest {

    // DataProvider fetches data from Excel
    @DataProvider(name = "Login data")
    public Object[][] getLoginData() {
        String projectPath = System.getProperty("user.dir");
        String excelPath = projectPath + "/src/test/resources/TestData.xlsx";
        return ExcelUtils.getTestData(excelPath, "Login data");
    }

    // Test method accepts 3 parameters matching the Excel columns
    @Test(dataProvider = "Login data")
    public void testBankLogin(String username, String password, String isValidUser) {
        
        LoginPage loginPage = new LoginPage(driver);
        
        // Execute login
        loginPage.loginToBanking(username, password);
        
        // Dynamic assertion based on valid/invalid user flag from Excel
        if (isValidUser.equalsIgnoreCase("TRUE")) {
            // Assertion for valid user credentials
            boolean isDashboardDisplayed = loginPage.isOverviewDisplayed();
            Assert.assertTrue(isDashboardDisplayed, "Valid user login failed for: " + username);
        } else {
            // Assertion for invalid user credentials
            String errorMsg = loginPage.getErrorMessage();
            Assert.assertNotNull(errorMsg, "Error message missing for invalid user: " + username);
        }
    }
}