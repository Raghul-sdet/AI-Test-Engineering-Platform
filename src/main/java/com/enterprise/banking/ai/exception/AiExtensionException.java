package com.enterprise.banking.ai.exception;

import java.io.Serial;

/**
 * Standardized runtime exception wrapper for the AI Extension module.
 */
public class AiExtensionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AiExtensionException(String message) {
        super(message);
    }

    public AiExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}