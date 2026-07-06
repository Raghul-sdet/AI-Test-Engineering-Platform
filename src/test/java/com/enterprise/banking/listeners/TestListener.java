package com.enterprise.banking.listeners;

import com.enterprise.banking.utils.DriverManager;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Enterprise TestNG Listener for unified reporting and execution monitoring.
 * Hooks into the test lifecycle to capture screenshots on failure safely via ThreadLocal WebDriver.
 */
public class TestListener implements ITestListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOGGER.info("Starting Test Execution: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("Test Execution Passed: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.error("Test Execution Failed: {}", result.getMethod().getMethodName());
        
        // Fix: Retrieve the thread-safe WebDriver directly from the static DriverManager 
        // instead of attempting to access a non-static method from BaseTest.
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            saveScreenshotToAllure(driver);
            LOGGER.info("Failure screenshot captured successfully.");
        } else {
            LOGGER.warn("WebDriver instance is null; unable to capture failure screenshot.");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warn("Test Execution Skipped: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        LOGGER.info("Initializing TestNG Suite: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info("Completed TestNG Suite: {}", context.getName());
    }

    /**
     * Captures and attaches a screenshot to the Allure report securely.
     *
     * @param driver The active ThreadLocal WebDriver instance.
     * @return The raw byte array of the captured screenshot.
     */
    @Attachment(value = "Failure Screenshot", type = "image/png")
    private byte[] saveScreenshotToAllure(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}