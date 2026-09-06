package com.enterprise.banking.ai.exploration;

/**
 * Represents the current operational lifecycle state of an exploratory testing session.
 */
public enum ExplorationState {
    INITIALIZED,
    DISCOVERING,
    NAVIGATING,
    ANALYZING,
    LEARNING,
    COMPLETED,
    FAILED,
    ABORTED_DEAD_END,
    ABORTED_INFINITE_LOOP
}