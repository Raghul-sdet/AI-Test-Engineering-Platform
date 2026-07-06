package com.enterprise.banking.ai.execution;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain model representing the consolidated results of a TestNG suite execution.
 * Designed as a hybrid POJO to accommodate both Suite-level summary metrics for the Orchestrator
 * and Test-level granular execution details for legacy failure listeners.
 */
public class ExecutionReport {

    // ========================================================================
    // Suite-Level Execution Metrics
    // ========================================================================
    private String executionId;
    private int totalTests;
    private int passCount;
    private int failCount;
    private int skipCount;
    private LocalDateTime executionTimestamp;
    private String extentReportPath;
    private String allureReportPath;

    // ========================================================================
    // Test-Level Granular Metrics (Legacy Listener Support)
    // ========================================================================
    private String testName;
    private boolean isSuccess;
    private String errorMessage;
    private String stackTrace;
    private String screenshotPath;
    private String videoRecordingPath;
    private String executionLogs;

    /**
     * Default constructor required for framework instantiation.
     * Automatically initializes execution metadata identifiers and timestamps.
     */
    public ExecutionReport() {
        this.executionId = "EXEC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.executionTimestamp = LocalDateTime.now();
    }

    /**
     * Parameterized constructor for suite-level direct instantiation post-execution.
     *
     * @param totalTests The total number of test cases executed.
     * @param passCount  The total number of successful test cases.
     * @param failCount  The total number of failed test cases.
     * @param skipCount  The total number of skipped test cases.
     */
    public ExecutionReport(int totalTests, int passCount, int failCount, int skipCount) {
        this();
        this.totalTests = totalTests;
        this.passCount = passCount;
        this.failCount = failCount;
        this.skipCount = skipCount;
    }

    /**
     * Parameterized constructor explicitly mapped for legacy listener compatibility.
     * Captures granular test execution contexts such as errors and artifacts.
     *
     * @param testName           The descriptive name of the executed test.
     * @param isSuccess          The boolean flag indicating test success or failure.
     * @param errorMessage       The explicit failure message, if applicable.
     * @param stackTrace         The complete execution stack trace dump.
     * @param screenshotPath     The physical path to the captured failure screenshot.
     * @param videoRecordingPath The physical path to the test execution video recording.
     * @param executionLogs      The standard output logs generated during execution.
     */
    public ExecutionReport(String testName, boolean isSuccess, String errorMessage, 
                           String stackTrace, String screenshotPath, 
                           String videoRecordingPath, String executionLogs) {
        this();
        this.testName = testName;
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
        this.screenshotPath = screenshotPath;
        this.videoRecordingPath = videoRecordingPath;
        this.executionLogs = executionLogs;
    }

    // ========================================================================
    // Record-style accessors to maintain backward compatibility with legacy 
    // ExecutionCoordinator and AiFailureListener integrations.
    // ========================================================================

    public String testName() {
        return this.testName;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public String errorMessage() {
        return this.errorMessage;
    }

    public String screenshotPath() {
        return this.screenshotPath;
    }

    // ========================================================================
    // Standard Getters and Setters for modern framework usage and metrics
    // ========================================================================

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public LocalDateTime getExecutionTimestamp() {
        return executionTimestamp;
    }

    public void setExecutionTimestamp(LocalDateTime executionTimestamp) {
        this.executionTimestamp = executionTimestamp;
    }

    public String getExtentReportPath() {
        return extentReportPath;
    }

    public void setExtentReportPath(String extentReportPath) {
        this.extentReportPath = extentReportPath;
    }

    public String getAllureReportPath() {
        return allureReportPath;
    }

    public void setAllureReportPath(String allureReportPath) {
        this.allureReportPath = allureReportPath;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }

    public String getVideoRecordingPath() {
        return videoRecordingPath;
    }

    public void setVideoRecordingPath(String videoRecordingPath) {
        this.videoRecordingPath = videoRecordingPath;
    }

    public String getExecutionLogs() {
        return executionLogs;
    }

    public void setExecutionLogs(String executionLogs) {
        this.executionLogs = executionLogs;
    }
}