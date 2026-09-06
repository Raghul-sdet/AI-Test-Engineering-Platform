package com.enterprise.banking.ai.data.model;

/**
 * A Data Transfer Object explicitly defining the parameters and bounds
 * required for the AI engine to generate contextual test data.
 */
public class DataGenerationRequest {

    private String scenarioId;
    private String targetEntity;
    private boolean requiresNegativeData;
    private boolean requiresBoundaryData;
    private boolean requiresEdgeCases;
    private int requestedRecordCount;

    /**
     * Default constructor initializing basic defaults.
     */
    public DataGenerationRequest() {
        this.requestedRecordCount = 1;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(String targetEntity) {
        this.targetEntity = targetEntity;
    }

    public boolean isRequiresNegativeData() {
        return requiresNegativeData;
    }

    public void setRequiresNegativeData(boolean requiresNegativeData) {
        this.requiresNegativeData = requiresNegativeData;
    }

    public boolean isRequiresBoundaryData() {
        return requiresBoundaryData;
    }

    public void setRequiresBoundaryData(boolean requiresBoundaryData) {
        this.requiresBoundaryData = requiresBoundaryData;
    }

    public boolean isRequiresEdgeCases() {
        return requiresEdgeCases;
    }

    public void setRequiresEdgeCases(boolean requiresEdgeCases) {
        this.requiresEdgeCases = requiresEdgeCases;
    }

    public int getRequestedRecordCount() {
        return requestedRecordCount;
    }

    public void setRequestedRecordCount(int requestedRecordCount) {
        if (requestedRecordCount <= 0) {
            throw new IllegalArgumentException("Record count must be at least 1.");
        }
        this.requestedRecordCount = requestedRecordCount;
    }
}