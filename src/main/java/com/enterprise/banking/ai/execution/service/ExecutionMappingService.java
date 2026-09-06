package com.enterprise.banking.ai.execution.service;

import com.enterprise.banking.ai.exception.ExecutionMappingException;
import com.enterprise.banking.ai.execution.mapping.ExecutionPlanBuilder;
import com.enterprise.banking.ai.execution.mapping.ExecutionPriorityEngine;
import com.enterprise.banking.ai.execution.mapping.ScenarioToPageMapper;
import com.enterprise.banking.ai.execution.mapping.ScenarioToTestMapper;
import com.enterprise.banking.ai.execution.model.ExecutionPlan;
import com.enterprise.banking.ai.execution.model.MappedScenario;
import com.enterprise.banking.ai.execution.registry.PageObjectRegistry;
import com.enterprise.banking.ai.execution.registry.TestRegistry;
import com.enterprise.banking.ai.model.GeneratedScenario;
import com.enterprise.banking.ai.model.ScenarioCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main orchestrator service that translates in-memory AI generated scenarios
 * into actionable, prioritized Execution Plans bound to existing automation code.
 */
public class ExecutionMappingService {

    private static final Logger LOGGER = Logger.getLogger(ExecutionMappingService.class.getName());

    private final ScenarioToTestMapper testMapper;
    private final ScenarioToPageMapper pageMapper;
    private final ExecutionPriorityEngine priorityEngine;

    /**
     * Initializes the service, instantiating necessary registries and mapper components.
     */
    public ExecutionMappingService() {
        try {
            TestRegistry testRegistry = new TestRegistry();
            PageObjectRegistry pageRegistry = new PageObjectRegistry();

            this.testMapper = new ScenarioToTestMapper(testRegistry);
            this.pageMapper = new ScenarioToPageMapper(pageRegistry);
            this.priorityEngine = new ExecutionPriorityEngine();
            
            LOGGER.info("ExecutionMappingService initialized successfully.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to initialize Execution Mapping Service components.", ex);
            throw new IllegalStateException("Critical failure during Mapping Service startup.", ex);
        }
    }

    /**
     * Processes an entire collection of scenarios, mapping each to existing assets
     * and generating a prioritized list of executable test plans.
     *
     * @param collection The collection of generated scenarios
     * @return A prioritized list of comprehensive Execution Plans
     * @throws ExecutionMappingException if mapping fails or inputs are invalid
     */
    public List<ExecutionPlan> mapScenariosToExecutionPlans(ScenarioCollection collection) {
        if (collection == null || collection.getScenarios() == null || collection.getScenarios().isEmpty()) {
            LOGGER.severe("Mapping aborted: Scenario collection is null or empty.");
            throw new ExecutionMappingException("Cannot map execution plans for empty scenario collections.");
        }

        List<ExecutionPlan> executionPlans = new ArrayList<>();
        LOGGER.info("Initiating execution mapping for " + collection.getTotalScenarios() + " scenarios.");

        try {
            for (GeneratedScenario scenario : collection.getScenarios()) {
                ExecutionPlan plan = processSingleScenario(scenario);
                executionPlans.add(plan);
            }

            LOGGER.info("Applying priority ordering to " + executionPlans.size() + " generated plans.");
            priorityEngine.prioritizeAndOrder(executionPlans);

            return executionPlans;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected error during execution mapping pipeline.", ex);
            throw new ExecutionMappingException("Mapping pipeline execution failed.", ex);
        }
    }

    private ExecutionPlan processSingleScenario(GeneratedScenario scenario) {
        String testClass = testMapper.mapScenario(scenario);
        String pageObject = pageMapper.mapScenario(scenario);

        // Assume confidence is high if default fallback wasn't used
        double confidence = (testClass.contains("BaseTest") && pageObject.contains("BasePage")) ? 30.0 : 95.0;

        MappedScenario mappedScenario = new MappedScenario(scenario, testClass, pageObject, confidence);

        return ExecutionPlanBuilder.start()
                .withMappedScenario(mappedScenario)
                .buildTasks()
                .getPlan();
    }
}