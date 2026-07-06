package com.enterprise.banking.ai.service;

import com.enterprise.banking.ai.model.TestScenario;
import java.util.List;

/**
 * Business service layer exposing operations for AI QA generations.
 */
public interface TestScenarioGenerator {
    /**
     * Synthesizes professional test scenarios for a given feature scope.
     *
     * @param featureName Name of target feature (e.g., "Transfer Funds")
     * @return A list of structured TestScenario domain elements.
     */
    List<TestScenario> generateScenarios(String featureName);
}