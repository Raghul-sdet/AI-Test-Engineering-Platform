package com.enterprise.banking.ai.healing.model;

/**
 * Contains the outcome of a discrete self-healing attempt.
 */
public class HealingResult {

    private String executionId;
    private String originalLocator;
    private LocatorCandidate appliedLocator;
    private HealingAction actionTaken;
    private boolean successful;
    private int retryCount;
    private long executionTimeMillis;

    public HealingResult() {
        this.actionTaken = HealingAction.NOT_APPLICABLE;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getOriginalLocator() {
        return originalLocator;
    }

    public void setOriginalLocator(String originalLocator) {
        this.originalLocator = originalLocator;
    }

    public LocatorCandidate getAppliedLocator() {
        return appliedLocator;
    }

    public void setAppliedLocator(LocatorCandidate appliedLocator) {
        this.appliedLocator = appliedLocator;
    }

    public HealingAction getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(HealingAction actionTaken) {
        this.actionTaken = actionTaken;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getExecutionTimeMillis() {
        return executionTimeMillis;
    }

    public void setExecutionTimeMillis(long executionTimeMillis) {
        this.executionTimeMillis = executionTimeMillis;
    }
}