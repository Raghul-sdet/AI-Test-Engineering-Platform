package com.enterprise.banking.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.enterprise.banking.tests.BaseTest;
import com.enterprise.banking.utils.AllureUtils;
import com.enterprise.banking.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.createInstance();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public synchronized void onTestStart(ITestResult result) {
        // Initialize ExtentTest instance
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test Passed Successfully");
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        // Securely fetch the specific WebDriver instance belonging to this exact failing thread
        WebDriver driver = BaseTest.getDriver();
        
        if (driver != null) {
            // --- 1. Extent Reports Attachment ---
            String base64Screenshot = ScreenshotUtils.getBase64Screenshot(driver);
            test.get().fail("Test Failed. See screenshot below:", 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            
            // --- 2. Allure Reports Attachment ---
            AllureUtils.saveScreenshot(driver);
            AllureUtils.savePageSource(driver);
            AllureUtils.saveBrowserLogs(driver);
        }
        
        // Log exception in Extent Reports
        test.get().log(Status.FAIL, "Exception: " + result.getThrowable());
        
        // Log exception in Allure Reports
        if (result.getThrowable() != null) {
            AllureUtils.saveTextLog("Test failed due to: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        // Log skip in Extent Reports
        test.get().log(Status.SKIP, "Test Skipped: " + result.getThrowable());
        
        // Log skip in Allure Reports
        if (result.getThrowable() != null) {
            AllureUtils.saveTextLog("Test was skipped due to: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        // Flush existing Extent Reports
        extent.flush();
        
        // Generate Allure Environment properties
        AllureUtils.generateEnvironmentDetails();
    }
}