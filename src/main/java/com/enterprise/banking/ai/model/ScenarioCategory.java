package com.enterprise.banking.ai.model;

/**
 * Represents the distinct categories of test scenarios that can be generated
 * by the AI Test Scenario Generation Engine. These categories ensure comprehensive
 * test coverage across different functional and non-functional vectors.
 */
public enum ScenarioCategory {
    /**
     * Standard valid workflows and expected behaviors.
     */
    POSITIVE,

    /**
     * Invalid inputs, error handling, and alternate flows.
     */
    NEGATIVE,

    /**
     * Edge cases and system limits.
     */
    BOUNDARY,

    /**
     * Validation of existing features against new changes.
     */
    REGRESSION,

    /**
     * Vulnerability checks, authentication, and authorization validations.
     */
    SECURITY,

    /**
     * Load, stress, and response time validations.
     */
    PERFORMANCE,

    /**
     * System restoration and fault tolerance checks.
     */
    RECOVERY,

    /**
     * User experience and interface accessibility validations.
     */
    USABILITY
}