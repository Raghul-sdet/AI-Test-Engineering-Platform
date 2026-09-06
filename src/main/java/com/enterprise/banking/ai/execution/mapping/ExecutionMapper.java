package com.enterprise.banking.ai.execution.mapping;

import com.enterprise.banking.ai.model.GeneratedScenario;

/**
 * Defines the contract for mapping an AI-generated scenario to an existing framework asset.
 */
public interface ExecutionMapper {

    /**
     * Analyzes a scenario and returns the fully qualified name of the matched automation asset.
     *
     * @param scenario The AI-generated scenario to analyze
     * @return Fully qualified class or resource name
     */
    String mapScenario(GeneratedScenario scenario);
}