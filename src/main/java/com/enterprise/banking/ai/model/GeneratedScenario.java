package com.enterprise.banking.ai.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Represents a single, executable AI-generated business test scenario.
 * It encapsulates the scenario metadata, execution priority, testing category,
 * and the sequential list of steps required to execute the test.
 */
public class GeneratedScenario {

    private static final Logger LOGGER = Logger.getLogger(GeneratedScenario.class.getName());

    private String scenarioId;
    private String scenarioName;
    private ScenarioPriority priority;
    private ScenarioCategory category;
    private String description;
    private String expectedResult;
    private List<GeneratedStep> steps;

    /**
     * Default constructor initializing unique identifier and step collection.
     */
    public GeneratedScenario() {
        this.scenarioId = "SCEN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.steps = new ArrayList<>();
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        if (scenarioName == null || scenarioName.trim().isEmpty()) {
            throw new IllegalArgumentException("Scenario name cannot be null or empty.");
        }
        this.scenarioName = scenarioName;
    }

    public ScenarioPriority getPriority() {
        return priority;
    }

    public void setPriority(ScenarioPriority priority) {
        this.priority = priority;
    }

    public ScenarioCategory getCategory() {
        return category;
    }

    public void setCategory(ScenarioCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public List<GeneratedStep> getSteps() {
        return steps;
    }

    public void setSteps(List<GeneratedStep> steps) {
        if (steps == null) {
            throw new IllegalArgumentException("Steps collection cannot be null.");
        }
        this.steps = steps;
    }

    /**
     * Adds a single generated step to this scenario.
     *
     * @param step The step to add
     */
    public void addStep(GeneratedStep step) {
        if (step != null) {
            this.steps.add(step);
        } else {
            LOGGER.warning("Attempted to add a null step to scenario: " + this.scenarioId);
        }
    }
}