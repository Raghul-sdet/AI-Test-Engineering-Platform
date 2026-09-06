package com.enterprise.banking.ai.exception;

/**
 * Custom exception to handle critical errors during the dynamic AI self-healing process.
 */
public class SelfHealingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the exception.
     */
    public SelfHealingException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public SelfHealingException(String message, Throwable cause) {
        super(message, cause);
    }
}