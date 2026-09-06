package com.enterprise.banking.ai.execution.model;

/**
 * Represents the lifecycle state of an execution plan or task.
 */
public enum ExecutionStatus {
    /**
     * Task or plan is created but not yet queued.
     */
    PENDING,

    /**
     * Task or plan is currently executing.
     */
    IN_PROGRESS,

    /**
     * Task or plan executed successfully without errors.
     */
    COMPLETED,

    /**
     * Task or plan encountered an error or assertion failure during execution.
     */
    FAILED,

    /**
     * Task or plan was skipped based on conditional rules or previous failures.
     */
    SKIPPED
}