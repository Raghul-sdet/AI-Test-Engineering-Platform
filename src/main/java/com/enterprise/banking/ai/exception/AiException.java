package com.enterprise.banking.ai.exception;

/**
 * Custom exception to handle errors and validation failures during the 
 * LLM Integration Layer operations (e.g., prompt validation, API connection).
 */
public class AiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new AI exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public AiException(String message) {
        super(message);
    }

    /**
     * Constructs a new AI exception with the specified detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public AiException(String message, Throwable cause) {
        super(message, cause);
    }
}