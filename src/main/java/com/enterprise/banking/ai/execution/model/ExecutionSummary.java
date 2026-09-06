package com.enterprise.banking.ai.execution.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Encapsulates the overall result of an execution orchestration cycle.
 */
public class ExecutionSummary {

    private String executionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long durationMillis;
    private int totalTests;
    private int passed;
    private int failed;
    private int skipped;
    private double successRate;

    /**
     * Default constructor initializing execution identifiers and start time.
     */
    public ExecutionSummary() {
        this.executionId = "EXEC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.startTime = LocalDateTime.now();
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }
}