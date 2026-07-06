package com.enterprise.banking.ai.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the foundational Test Plan entity in the Enterprise AI QA Framework.
 * Acts as a hybrid POJO providing both standard JavaBean accessors for ORM/Serialization 
 * and record-style accessors for legacy framework compatibility.
 */
public class TestPlan {

    // Standard POJO Fields
    private String planId;
    private String originalRequirement;
    private String aiContext;
    private LocalDateTime creationTimestamp;
    private String planStatus;

    // Framework specific mapping fields
    private String requirementId;
    private String featureName;
    private List<TestScenario> scenarios;
    private Map<String, List<TestCase>> testCasesByScenarioId;

    /**
     * Default constructor required for framework instantiation and serialization.
     * Initializes lists and maps to prevent NullPointerExceptions.
     */
    public TestPlan() {
        this.scenarios = new ArrayList<>();
        this.testCasesByScenarioId = new HashMap<>();
    }

    /**
     * Parameterized constructor required by legacy framework classification engines.
     *
     * @param requirementId         The unique identifier for the requirement.
     * @param featureName           The name of the feature or original requirement.
     * @param scenarios             The list of generated test scenarios.
     * @param testCasesByScenarioId A mapped collection of test cases grouped by scenario ID.
     */
    public TestPlan(String requirementId, String featureName, List<TestScenario> scenarios, Map<String, List<TestCase>> testCasesByScenarioId) {
        this.requirementId = requirementId;
        this.featureName = featureName;
        this.scenarios = scenarios != null ? scenarios : new ArrayList<>();
        this.testCasesByScenarioId = testCasesByScenarioId != null ? testCasesByScenarioId : new HashMap<>();
        
        // Synchronize legacy fields with modern POJO fields
        this.planId = requirementId;
        this.originalRequirement = featureName;
    }

    // ========================================================================
    // Record-style accessors to maintain backward compatibility with 
    // DatabaseToExcelExporter, AiDatabasePersister, and CoverageAnalyzer
    // ========================================================================

    /**
     * Retrieves the requirement ID.
     * @return The string representing the requirement ID.
     */
    public String requirementId() {
        return this.requirementId != null ? this.requirementId : this.planId;
    }

    /**
     * Retrieves the feature name.
     * @return The string representing the feature name or original requirement.
     */
    public String featureName() {
        return this.featureName != null ? this.featureName : this.originalRequirement;
    }

    /**
     * Retrieves the list of associated test scenarios.
     * @return A List of TestScenario objects.
     */
    public List<TestScenario> scenarios() {
        return this.scenarios;
    }

    /**
     * Retrieves the mapped test cases by their scenario ID.
     * @return A Map containing test cases linked to scenario IDs.
     */
    public Map<String, List<TestCase>> testCasesByScenarioId() {
        return this.testCasesByScenarioId;
    }

    // ========================================================================
    // Standard Getters and Setters for modern framework usage
    // ========================================================================

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
        this.requirementId = planId; // Keep synchronized
    }

    public String getOriginalRequirement() {
        return originalRequirement;
    }

    public void setOriginalRequirement(String originalRequirement) {
        this.originalRequirement = originalRequirement;
        this.featureName = originalRequirement; // Keep synchronized
    }

    public String getAiContext() {
        return aiContext;
    }

    public void setAiContext(String aiContext) {
        this.aiContext = aiContext;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
        this.planId = requirementId;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
        this.originalRequirement = featureName;
    }

    public void setScenarios(List<TestScenario> scenarios) {
        this.scenarios = scenarios != null ? scenarios : new ArrayList<>();
    }

    public void setTestCasesByScenarioId(Map<String, List<TestCase>> testCasesByScenarioId) {
        this.testCasesByScenarioId = testCasesByScenarioId != null ? testCasesByScenarioId : new HashMap<>();
    }
}