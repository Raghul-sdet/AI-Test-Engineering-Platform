package com.enterprise.banking.ai.exception;

/**
 * Custom exception to handle errors and validation failures during the 
 * AI test execution mapping and plan generation process.
 */
public class ExecutionMappingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the exception.
     */
    public ExecutionMappingException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public ExecutionMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}