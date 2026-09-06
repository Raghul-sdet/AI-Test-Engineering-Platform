package com.enterprise.banking.ai.execution.model;

import com.enterprise.banking.ai.model.GeneratedScenario;

/**
 * Encapsulates a GeneratedScenario alongside its discovered automation targets
 * within the existing framework (Test Class and Page Object).
 */
public class MappedScenario {

    private GeneratedScenario scenario;
    private String mappedTestClass;
    private String mappedPageObject;
    private double matchConfidence;

    /**
     * Default constructor.
     */
    public MappedScenario() {
    }

    /**
     * Constructs a mapped scenario entity.
     *
     * @param scenario         The original AI-generated scenario
     * @param mappedTestClass  The discovered matching TestNG test class
     * @param mappedPageObject The discovered matching Selenium Page Object
     * @param matchConfidence  The confidence level of the mapping engine (0.0 to 100.0)
     */
    public MappedScenario(GeneratedScenario scenario, String mappedTestClass, String mappedPageObject, double matchConfidence) {
        if (scenario == null) {
            throw new IllegalArgumentException("GeneratedScenario cannot be null for mapping.");
        }
        this.scenario = scenario;
        this.mappedTestClass = mappedTestClass;
        this.mappedPageObject = mappedPageObject;
        this.matchConfidence = matchConfidence;
    }

    public GeneratedScenario getScenario() {
        return scenario;
    }

    public void setScenario(GeneratedScenario scenario) {
        this.scenario = scenario;
    }

    public String getMappedTestClass() {
        return mappedTestClass;
    }

    public void setMappedTestClass(String mappedTestClass) {
        this.mappedTestClass = mappedTestClass;
    }

    public String getMappedPageObject() {
        return mappedPageObject;
    }

    public void setMappedPageObject(String mappedPageObject) {
        this.mappedPageObject = mappedPageObject;
    }

    public double getMatchConfidence() {
        return matchConfidence;
    }

    public void setMatchConfidence(double matchConfidence) {
        this.matchConfidence = matchConfidence;
    }
}