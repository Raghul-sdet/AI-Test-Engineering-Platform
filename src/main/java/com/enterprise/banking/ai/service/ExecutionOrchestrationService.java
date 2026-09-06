package com.enterprise.banking.ai.service;

import com.enterprise.banking.ai.exception.ExecutionOrchestratorException;
import com.enterprise.banking.ai.execution.model.ExecutionMetrics;
import com.enterprise.banking.ai.execution.model.ExecutionPlan;
import com.enterprise.banking.ai.execution.model.ExecutionSummary;
import com.enterprise.banking.ai.execution.orchestrator.DynamicTestNGOrchestrator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service facade exposing orchestration capabilities to the broader AI framework.
 */
public class ExecutionOrchestrationService {

    private static final Logger LOGGER = Logger.getLogger(ExecutionOrchestrationService.class.getName());
    private final DynamicTestNGOrchestrator orchestrator;

    /**
     * Initializes the service and its underlying orchestrator engine.
     */
    public ExecutionOrchestrationService() {
        this.orchestrator = new DynamicTestNGOrchestrator();
        LOGGER.info("ExecutionOrchestrationService is ready.");
    }

    /**
     * Takes finalized ExecutionPlans and programmatically triggers TestNG execution.
     *
     * @param executionPlans The list of populated ExecutionPlans to run
     * @return The ExecutionSummary detailing pass/fail ratios and times
     * @throws ExecutionOrchestratorException on critical orchestration failure
     */
    public ExecutionSummary executeDynamicTestSuite(List<ExecutionPlan> executionPlans) {
        if (executionPlans == null || executionPlans.isEmpty()) {
            throw new ExecutionOrchestratorException("Cannot orchestrate an empty or null list of execution plans.");
        }

        try {
            LOGGER.info("Passing " + executionPlans.size() + " plans to the Orchestrator Engine.");
            return orchestrator.orchestrate(executionPlans);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Fatal error occurred within orchestration pipeline.", ex);
            throw new ExecutionOrchestratorException("Dynamic test suite execution failed.", ex);
        }
    }

    /**
     * Retrieves extended metrics calculated after a test suite execution.
     *
     * @return Populated ExecutionMetrics object
     */
    public ExecutionMetrics getExecutionMetrics() {
        return orchestrator.getResultCollector().buildMetrics();
    }
}