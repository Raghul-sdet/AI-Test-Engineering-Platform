package com.enterprise.banking.ai.execution;

import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Custom listener dedicated to capturing AI-specific failure telemetry.
 * Integrates directly with the dynamic TestNG execution.
 */
public class AiFailureListener implements ITestListener {

    private ExecutionReport lastReport;

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("[AI-TELEMETRY] Test Failed. Capturing execution state for AI analysis.");
        
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown Error";
        
        // In a full implementation, these would extract driver state via thread-local BaseTest instance
        String screenshotPath = "/reports/ai-screenshots/" + testName + ".png";
        String domStateSnapshot = "<html><body>Captured DOM snapshot...</body></html>";
        
        this.lastReport = new ExecutionReport(
                testName, false, errorMessage, "Dynamic Step", "Dynamic Locator", screenshotPath, domStateSnapshot
        );
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        this.lastReport = new ExecutionReport(
                result.getMethod().getMethodName(), true, null, null, null, null, null
        );
    }

    public ExecutionReport getLastReport() {
        return lastReport;
    }
}