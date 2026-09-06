package com.enterprise.banking.ai.model;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a single executable step within an AI-generated test scenario.
 * It contains the sequential execution order, the specific user or system action,
 * and the expected outcome for validation.
 */
public class GeneratedStep {

    private static final Logger LOGGER = Logger.getLogger(GeneratedStep.class.getName());

    private int stepNumber;
    private String action;
    private String expectedResult;

    /**
     * Default constructor for serialization and framework compatibility.
     */
    public GeneratedStep() {
    }

    /**
     * Parameterized constructor with strict validation.
     *
     * @param stepNumber     The sequence number of the step (must be greater than 0)
     * @param action         The action to be executed
     * @param expectedResult The expected outcome for validation
     */
    public GeneratedStep(int stepNumber, String action, String expectedResult) {
        setStepNumber(stepNumber);
        setAction(action);
        setExpectedResult(expectedResult);
        LOGGER.log(Level.FINE, "GeneratedStep initialized: Step {0}", this.stepNumber);
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        if (stepNumber <= 0) {
            LOGGER.severe("Validation Failure: Step number must be strictly positive. Provided: " + stepNumber);
            throw new IllegalArgumentException("Step number must be strictly greater than zero.");
        }
        this.stepNumber = stepNumber;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        if (action == null || action.trim().isEmpty()) {
            LOGGER.severe("Validation Failure: Action description cannot be null or empty.");
            throw new IllegalArgumentException("Action description cannot be null or empty.");
        }
        this.action = action;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        if (expectedResult == null || expectedResult.trim().isEmpty()) {
            LOGGER.severe("Validation Failure: Expected result cannot be null or empty.");
            throw new IllegalArgumentException("Expected result cannot be null or empty.");
        }
        this.expectedResult = expectedResult;
    }

    @Override
    public String toString() {
        return String.format("Step %d: %s -> Expected: %s", stepNumber, action, expectedResult);
    }
}