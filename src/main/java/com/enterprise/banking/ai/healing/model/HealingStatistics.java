package com.enterprise.banking.ai.healing.model;

/**
 * Tracks cumulative statistics for the Self-Healing Engine across an execution suite.
 */
public class HealingStatistics {

    private int totalHealingAttempts;
    private int successfulHealings;
    private int failedHealings;
    private long totalHealingTimeMillis;

    public synchronized void recordAttempt(boolean success, long timeMillis) {
        this.totalHealingAttempts++;
        this.totalHealingTimeMillis += timeMillis;
        if (success) {
            this.successfulHealings++;
        } else {
            this.failedHealings++;
        }
    }

    public int getTotalHealingAttempts() {
        return totalHealingAttempts;
    }

    public int getSuccessfulHealings() {
        return successfulHealings;
    }

    public int getFailedHealings() {
        return failedHealings;
    }

    public long getTotalHealingTimeMillis() {
        return totalHealingTimeMillis;
    }
}