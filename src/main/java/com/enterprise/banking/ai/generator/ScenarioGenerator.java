package com.enterprise.banking.ai.generator;

import com.enterprise.banking.ai.model.GeneratedScenario;
import com.enterprise.banking.ai.model.Requirement;
import com.enterprise.banking.ai.model.RequirementFeature;
import com.enterprise.banking.ai.model.RequirementRisk;
import com.enterprise.banking.ai.model.ScenarioCollection;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Core engine responsible for translating a structured business requirement 
 * into a comprehensive suite of executable test scenarios.
 */
public class ScenarioGenerator {

    private static final Logger LOGGER = Logger.getLogger(ScenarioGenerator.class.getName());

    private final ScenarioRuleEngine ruleEngine;
    private final ScenarioTemplateEngine templateEngine;

    /**
     * Initializes the Scenario Generator with required structural engines.
     *
     * @param ruleEngine     Engine to apply contextual business rules
     * @param templateEngine Engine to build standardized test steps
     */
    public ScenarioGenerator(ScenarioRuleEngine ruleEngine, ScenarioTemplateEngine templateEngine) {
        if (ruleEngine == null || templateEngine == null) {
            throw new IllegalArgumentException("RuleEngine and TemplateEngine must be instantiated.");
        }
        this.ruleEngine = ruleEngine;
        this.templateEngine = templateEngine;
    }

    /**
     * Parses the provided requirement, analyzes features and risks, and generates
     * a fully populated collection of prioritized test scenarios.
     *
     * @param requirement The analyzed business requirement
     * @return A collection of generated business scenarios
     */
    public ScenarioCollection generateScenarios(Requirement requirement) {
        if (requirement == null || requirement.getId() == null) {
            LOGGER.severe("Invalid Requirement: Null object or missing Requirement ID.");
            throw new IllegalArgumentException("A valid Requirement with an ID is mandatory for scenario generation.");
        }

        LOGGER.log(Level.INFO, "Initiating scenario generation for Requirement: {0}", requirement.getTitle());
        ScenarioCollection collection = new ScenarioCollection(requirement.getId());

        processFeatures(requirement.getFeatures(), collection);
        processRisks(requirement.getRisks(), collection);

        LOGGER.log(Level.INFO, "Scenario generation completed successfully. Total Scenarios: {0}, Total Steps: {1}", 
                new Object[]{collection.getTotalScenarios(), collection.getTotalSteps()});

        return collection;
    }

    private void processFeatures(List<RequirementFeature> features, ScenarioCollection collection) {
        if (features == null || features.isEmpty()) {
            LOGGER.info("No explicit features found in requirement. Skipping feature scenario generation.");
            return;
        }

        for (RequirementFeature feature : features) {
            String evaluationContext = feature.getFeatureName() + " " + feature.getDescription();
            List<GeneratedScenario> baseScenarios = ruleEngine.applyRules(evaluationContext);

            for (GeneratedScenario scenario : baseScenarios) {
                scenario.setDescription(scenario.getDescription() + " (Mapped from Feature: " + feature.getFeatureId() + ")");
                templateEngine.applyTemplate(scenario);
                collection.addScenario(scenario);
            }
        }
    }

    private void processRisks(List<RequirementRisk> risks, ScenarioCollection collection) {
        if (risks == null || risks.isEmpty()) {
            LOGGER.info("No operational risks detected. Skipping risk mitigation scenario generation.");
            return;
        }

        for (RequirementRisk risk : risks) {
            String evaluationContext = risk.getRiskType() + " " + risk.getDescription() + " " + risk.getMitigation();
            List<GeneratedScenario> riskScenarios = ruleEngine.applyRules(evaluationContext);

            for (GeneratedScenario scenario : riskScenarios) {
                scenario.setDescription(scenario.getDescription() + " (Risk Mitigation for: " + risk.getRiskType() + ")");
                templateEngine.applyTemplate(scenario);
                collection.addScenario(scenario);
            }
        }
    }
}