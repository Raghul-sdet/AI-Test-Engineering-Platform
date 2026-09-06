package com.enterprise.banking.ai.exception;

/**
 * Custom exception to handle errors and validation failures during the 
 * AI test scenario generation process.
 */
public class ScenarioGenerationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public ScenarioGenerationException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and root cause.
     *
     * @param message the detail message explaining the reason for the exception.
     * @param cause   the underlying cause of the exception.
     */
    public ScenarioGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}