package com.enterprise.banking.ai.execution.model;

import java.time.LocalDateTime;

/**
 * Represents a historical record of a single mapped scenario execution.
 */
public class ExecutionHistory {

    private String executionId;
    private String scenarioId;
    private LocalDateTime executionTimestamp;
    private String executionStatus;
    private String mappedTest;

    /**
     * Default constructor.
     */
    public ExecutionHistory() {
        this.executionTimestamp = LocalDateTime.now();
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public LocalDateTime getExecutionTimestamp() {
        return executionTimestamp;
    }

    public void setExecutionTimestamp(LocalDateTime executionTimestamp) {
        this.executionTimestamp = executionTimestamp;
    }

    public String getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(String executionStatus) {
        this.executionStatus = executionStatus;
    }

    public String getMappedTest() {
        return mappedTest;
    }

    public void setMappedTest(String mappedTest) {
        this.mappedTest = mappedTest;
    }
}