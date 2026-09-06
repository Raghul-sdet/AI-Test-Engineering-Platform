package com.enterprise.banking.ai.exception;

/**
 * Custom exception to handle errors and validation failures during the 
 * AI Auto Defect Triaging and ticket generation process.
 */
public class DefectTriagerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the exception.
     */
    public DefectTriagerException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public DefectTriagerException(String message, Throwable cause) {
        super(message, cause);
    }
}