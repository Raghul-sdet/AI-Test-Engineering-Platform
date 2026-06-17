package com.enterprise.banking.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private static final int MAX_RETRY_COUNT = 2; // Retries failed tests twice

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (count < MAX_RETRY_COUNT) {
                count++;
                System.out.println("Retrying test " + iTestResult.getName() + " for the " + count + " time.");
                return true;
            }
        }
        return false;
    }
}