package com.enterprise.banking.ai.model;

/**
 * Represents the execution priority of a generated test scenario.
 * These priorities guide the test execution engine in scheduling
 * and filtering scenarios based on system criticality and business impact.
 */
public enum ScenarioPriority {
    
    /**
     * Low priority scenarios, typically minor UI validations, cosmetic checks, 
     * or extreme edge cases with minimal business impact.
     */
    LOW,

    /**
     * Medium priority scenarios covering standard alternate flows, 
     * secondary business rules, and non-critical negative paths.
     */
    MEDIUM,

    /**
     * High priority scenarios covering primary business workflows, 
     * standard positive paths, and important functional requirements.
     */
    HIGH,

    /**
     * Critical priority scenarios (e.g., Smoke/Sanity), encompassing core
     * functionality (Login, Payment) where failure indicates a broken build.
     */
    CRITICAL
}