package com.enterprise.banking.ai.execution.model;

/**
 * Contains advanced performance and stability metrics for a test execution suite.
 */
public class ExecutionMetrics {

    private double averageExecutionTimeMillis;
    private long fastestTestMillis;
    private long slowestTestMillis;
    private double failureRate;
    private double passRate;

    /**
     * Default constructor.
     */
    public ExecutionMetrics() {
    }

    public double getAverageExecutionTimeMillis() {
        return averageExecutionTimeMillis;
    }

    public void setAverageExecutionTimeMillis(double averageExecutionTimeMillis) {
        this.averageExecutionTimeMillis = averageExecutionTimeMillis;
    }

    public long getFastestTestMillis() {
        return fastestTestMillis;
    }

    public void setFastestTestMillis(long fastestTestMillis) {
        this.fastestTestMillis = fastestTestMillis;
    }

    public long getSlowestTestMillis() {
        return slowestTestMillis;
    }

    public void setSlowestTestMillis(long slowestTestMillis) {
        this.slowestTestMillis = slowestTestMillis;
    }

    public double getFailureRate() {
        return failureRate;
    }

    public void setFailureRate(double failureRate) {
        this.failureRate = failureRate;
    }

    public double getPassRate() {
        return passRate;
    }

    public void setPassRate(double passRate) {
        this.passRate = passRate;
    }
}