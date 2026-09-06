package com.enterprise.banking.ai.exception;

/**
 * Custom exception to handle errors, validation failures, and strategy routing
 * failures during the AI test data synthesis process.
 */
public class TestDataGenerationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the exception.
     */
    public TestDataGenerationException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public TestDataGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}