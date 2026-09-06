package com.enterprise.banking.ai.exception;

/**
 * Custom exception to handle errors during the AI requirement analysis process.
 */
public class RequirementAnalysisException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public RequirementAnalysisException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public RequirementAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}