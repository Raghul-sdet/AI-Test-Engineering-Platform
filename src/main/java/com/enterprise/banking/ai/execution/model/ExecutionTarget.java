package com.enterprise.banking.ai.execution.model;

/**
 * Defines the specific system layer or asset type targeted by an execution task.
 */
public enum ExecutionTarget {
    /**
     * Targets an existing TestNG test class execution.
     */
    TEST_CLASS,

    /**
     * Targets an existing Selenium Page Object for web interaction.
     */
    PAGE_OBJECT,

    /**
     * Targets an API endpoint for backend validation.
     */
    API_ENDPOINT,

    /**
     * Targets the backend database for state validation or setup.
     */
    DATABASE
}