package com.enterprise.banking.ai.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a comprehensive collection of generated scenarios derived from a specific requirement.
 * This class facilitates bulk operations, filtering, and metric tracking across the generated test suite.
 */
public class ScenarioCollection {

    private static final Logger LOGGER = Logger.getLogger(ScenarioCollection.class.getName());

    private String requirementId;
    private List<GeneratedScenario> scenarios;
    private int totalScenarios;
    private int totalSteps;

    /**
     * Default constructor for serialization and framework compatibility.
     */
    public ScenarioCollection() {
        this.scenarios = new ArrayList<>();
    }

    /**
     * Parameterized constructor initialized with the source requirement identifier.
     *
     * @param requirementId The unique identifier of the requirement these scenarios cover
     */
    public ScenarioCollection(String requirementId) {
        if (requirementId == null || requirementId.trim().isEmpty()) {
            throw new IllegalArgumentException("Requirement ID cannot be null or empty when creating a Scenario Collection.");
        }
        this.requirementId = requirementId;
        this.scenarios = new ArrayList<>();
        LOGGER.log(Level.INFO, "ScenarioCollection initialized for Requirement ID: {0}", this.requirementId);
    }

    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public List<GeneratedScenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<GeneratedScenario> scenarios) {
        if (scenarios == null) {
            throw new IllegalArgumentException("Scenarios collection cannot be null.");
        }
        this.scenarios = scenarios;
        calculateMetrics();
    }

    public int getTotalScenarios() {
        return totalScenarios;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    /**
     * Safely adds a scenario to the collection and updates metrics.
     *
     * @param scenario The generated scenario to add
     */
    public void addScenario(GeneratedScenario scenario) {
        if (scenario != null) {
            this.scenarios.add(scenario);
            calculateMetrics();
        } else {
            LOGGER.warning("Attempted to add a null scenario to the collection.");
        }
    }

    /**
     * Internal helper to recalculate totals after collection modifications.
     */
    private void calculateMetrics() {
        this.totalScenarios = this.scenarios.size();
        this.totalSteps = this.scenarios.stream()
                .filter(s -> s.getSteps() != null)
                .mapToInt(s -> s.getSteps().size())
                .sum();
    }
}