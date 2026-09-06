package com.enterprise.banking.ai.exception;

/**
 * Custom exception to handle errors and validation failures during the 
 * dynamic TestNG orchestration and execution pipeline.
 */
public class ExecutionOrchestratorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the exception.
     */
    public ExecutionOrchestratorException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public ExecutionOrchestratorException(String message, Throwable cause) {
        super(message, cause);
    }
}