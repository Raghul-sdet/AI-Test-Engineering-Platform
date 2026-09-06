package com.enterprise.banking.listeners;

import com.enterprise.banking.ai.export.ExecutionResultExporter;
import com.enterprise.banking.ai.model.TestResultRecord;
import com.enterprise.banking.utils.DriverManager;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Enterprise TestNG Listener for unified reporting and execution monitoring.
 * Hooks into the test lifecycle to capture screenshots on failure safely via ThreadLocal WebDriver.
 */
public class TestListener implements ITestListener, IExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);
    
    private static final List<TestResultRecord> EXECUTIONS = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void onTestStart(ITestResult result) {
        LOGGER.info("Starting Test Execution: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("Test Execution Passed: {}", result.getMethod().getMethodName());
        recordResult(result, "PASS", null);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.error("Test Execution Failed: {}", result.getMethod().getMethodName());
        
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            saveScreenshotToAllure(driver);
            LOGGER.info("Failure screenshot captured successfully.");
        } else {
            LOGGER.warn("WebDriver instance is null; unable to capture failure screenshot.");
        }
        
        recordResult(result, "FAIL", result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown Error");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warn("Test Execution Skipped: {}", result.getMethod().getMethodName());
        recordResult(result, "SKIP", result.getThrowable() != null ? result.getThrowable().getMessage() : "Skipped");
    }

    private void recordResult(ITestResult result, String status, String failureReason) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        long durationMs = result.getEndMillis() - result.getStartMillis();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(result.getStartMillis()));
        EXECUTIONS.add(new TestResultRecord(testName, className, status, durationMs, failureReason, timestamp));
    }

    @Override
    public void onStart(ITestContext context) {
        LOGGER.info("Initializing TestNG Context: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info("Completed TestNG Context: {}", context.getName());
    }

    @Override
    public void onExecutionStart() {
        LOGGER.info("Starting Global Execution");
        // Initialize the H2 database schema exactly once for the entire execution
        com.enterprise.banking.utils.DatabaseSetupUtility.initializeDatabase();
    }

    @Override
    public void onExecutionFinish() {
        LOGGER.info("Completed Global Execution. Triggering final Excel Report generation.");
        try {
            ExecutionResultExporter exporter = new ExecutionResultExporter();
            String excelFilePath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "reports" + File.separator + "Professional_Enterprise_Report.xlsx";
            exporter.appendExecutionResultsToExcel(EXECUTIONS, excelFilePath);
            LOGGER.info("Successfully updated Enterprise Excel Report at: {}", excelFilePath);
        } catch (Exception e) {
            LOGGER.error("Failed to append execution results to Excel report.", e);
        }
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