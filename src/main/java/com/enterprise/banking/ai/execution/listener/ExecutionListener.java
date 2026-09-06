package com.enterprise.banking.ai.execution.listener;

import com.enterprise.banking.ai.execution.runtime.RuntimeStatistics;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Custom TestNG listener to hook into dynamic runtime events and collect execution metrics
 * without interfering with existing framework reporting suites.
 */
public class ExecutionListener implements ITestListener {

    private final RuntimeStatistics statistics;

    /**
     * Constructor accepting a reference to the centralized runtime statistics tracker.
     *
     * @param statistics The data structure holding live execution results
     */
    public ExecutionListener(RuntimeStatistics statistics) {
        if (statistics == null) {
            throw new IllegalArgumentException("RuntimeStatistics cannot be null.");
        }
        this.statistics = statistics;
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExecutionLogger.logExecutionStart("N/A", result.getTestClass().getName() + "." + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        statistics.incrementPassed();
        statistics.addTiming(duration);
        ExecutionLogger.logExecutionFinish("N/A", duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        statistics.incrementFailed();
        statistics.addTiming(duration);
        ExecutionLogger.logExecutionFailure("N/A", result.getTestClass().getName(), result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown Failure");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        statistics.incrementSkipped();
        ExecutionLogger.logExecutionSkipped("N/A", result.getTestClass().getName());
    }

    @Override
    public void onStart(ITestContext context) {
        // Initialization if required
    }

    @Override
    public void onFinish(ITestContext context) {
        // Cleanup if required
    }
}