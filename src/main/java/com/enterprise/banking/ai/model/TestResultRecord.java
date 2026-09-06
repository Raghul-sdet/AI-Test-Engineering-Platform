package com.enterprise.banking.ai.model;

public class TestResultRecord {
    private final String testName;
    private final String className;
    private final String status;
    private final long durationMs;
    private final String failureReason;
    private final String timestamp;

    public TestResultRecord(String testName, String className, String status, long durationMs, String failureReason, String timestamp) {
        this.testName = testName;
        this.className = className;
        this.status = status;
        this.durationMs = durationMs;
        this.failureReason = failureReason;
        this.timestamp = timestamp;
    }

    public String getTestName() {
        return testName;
    }

    public String getClassName() {
        return className;
    }

    public String getStatus() {
        return status;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
