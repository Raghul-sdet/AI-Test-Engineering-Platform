package com.enterprise.banking.ai.healing.model;

/**
 * Represents the specific action taken by the AI engine to attempt recovery.
 */
public enum HealingAction {
    /** Attempted a simple execution retry (e.g., for StaleElementReferenceException). */
    RETRY_EXECUTED,
    
    /** Generated and applied a completely new fallback locator. */
    LOCATOR_REPLACED,
    
    /** Attempted to refresh the DOM context before locating. */
    DOM_REFRESHED,
    
    /** The healing process was attempted but failed to recover the execution. */
    HEALING_FAILED,
    
    /** No healing was necessary or applicable. */
    NOT_APPLICABLE
}