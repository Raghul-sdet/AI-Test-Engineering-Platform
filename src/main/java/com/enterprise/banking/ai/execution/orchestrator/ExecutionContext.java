package com.enterprise.banking.ai.execution.orchestrator;

import com.enterprise.banking.ai.execution.model.ExecutionPlan;
import java.time.LocalDateTime;

/**
 * Represents the wrapper context for a plan actively scheduled in the orchestrator.
 */
public class ExecutionContext {

    private final ExecutionPlan executionPlan;
    private final LocalDateTime queuedTime;

    /**
     * Initializes the context wrapper.
     *
     * @param executionPlan The plan to wrap
     */
    public ExecutionContext(ExecutionPlan executionPlan) {
        if (executionPlan == null) {
            throw new IllegalArgumentException("ExecutionPlan cannot be null.");
        }
        this.executionPlan = executionPlan;
        this.queuedTime = LocalDateTime.now();
    }

    public ExecutionPlan getExecutionPlan() {
        return executionPlan;
    }

    public LocalDateTime getQueuedTime() {
        return queuedTime;
    }
}