package com.enterprise.banking.ai.service;

import com.enterprise.banking.ai.exception.ScenarioGenerationException;
import com.enterprise.banking.ai.generator.ScenarioGenerator;
import com.enterprise.banking.ai.generator.ScenarioRuleEngine;
import com.enterprise.banking.ai.generator.ScenarioTemplateEngine;
import com.enterprise.banking.ai.model.Requirement;
import com.enterprise.banking.ai.model.ScenarioCollection;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Orchestrator service that manages the lifecycle of AI test scenario generation.
 * Coordinates the rule engine, template engine, and core generator to translate
 * business requirements into executable test suites.
 */
public class ScenarioGenerationService {

    private static final Logger LOGGER = Logger.getLogger(ScenarioGenerationService.class.getName());

    private final ScenarioGenerator generator;

    /**
     * Default constructor initializing the required generation engines.
     */
    public ScenarioGenerationService() {
        try {
            ScenarioRuleEngine ruleEngine = new ScenarioRuleEngine();
            ScenarioTemplateEngine templateEngine = new ScenarioTemplateEngine();
            this.generator = new ScenarioGenerator(ruleEngine, templateEngine);
            LOGGER.info("ScenarioGenerationService initialized successfully.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to initialize generation engines.", ex);
            throw new IllegalStateException("Critical failure during ScenarioGenerationService initialization.", ex);
        }
    }

    /**
     * Processes a fully analyzed business requirement and generates a comprehensive
     * collection of test scenarios based on business rules and templates.
     *
     * @param requirement The analyzed business requirement
     * @return ScenarioCollection containing all generated scenarios and metrics
     * @throws ScenarioGenerationException if generation fails or inputs are invalid
     */
    public ScenarioCollection generateScenariosForRequirement(Requirement requirement) {
        if (requirement == null) {
            LOGGER.severe("Generation aborted: Provided requirement is null.");
            throw new ScenarioGenerationException("Cannot generate scenarios for a null requirement.");
        }

        try {
            LOGGER.log(Level.INFO, "Starting scenario generation service for Requirement ID: {0}", requirement.getId());
            return generator.generateScenarios(requirement);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.SEVERE, "Validation error during scenario generation: {0}", ex.getMessage());
            throw new ScenarioGenerationException("Invalid requirement data prevented scenario generation.", ex);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected error during scenario generation.", ex);
            throw new ScenarioGenerationException("An unexpected error occurred while generating scenarios.", ex);
        }
    }
}