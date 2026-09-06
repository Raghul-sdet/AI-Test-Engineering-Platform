package com.enterprise.banking.ai.execution.mapping;

import com.enterprise.banking.ai.execution.registry.TestRegistry;
import com.enterprise.banking.ai.model.GeneratedScenario;
import com.enterprise.banking.ai.model.GeneratedStep;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Intelligent mapper that scans a GeneratedScenario to locate the most appropriate
 * existing TestNG test class in the framework.
 */
public class ScenarioToTestMapper implements ExecutionMapper {

    private static final Logger LOGGER = Logger.getLogger(ScenarioToTestMapper.class.getName());
    private final TestRegistry testRegistry;

    /**
     * Constructor initializing the required registry.
     *
     * @param testRegistry The test registry containing available framework classes.
     */
    public ScenarioToTestMapper(TestRegistry testRegistry) {
        if (testRegistry == null) {
            throw new IllegalArgumentException("TestRegistry cannot be null.");
        }
        this.testRegistry = testRegistry;
    }

    @Override
    public String mapScenario(GeneratedScenario scenario) {
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario cannot be null for test mapping.");
        }

        String searchContext = buildSearchContext(scenario);
        
        java.util.List<String> sortedKeywords = new java.util.ArrayList<>(testRegistry.getRegisteredKeywords());
        sortedKeywords.sort((k1, k2) -> Integer.compare(k2.length(), k1.length()));
        
        for (String keyword : sortedKeywords) {
            if (searchContext.contains(keyword)) {
                String mappedClass = testRegistry.getTestClass(keyword);
                LOGGER.log(Level.FINE, "Mapped scenario {0} to Test Class: {1} via keyword [{2}]",
                        new Object[]{scenario.getScenarioId(), mappedClass, keyword});
                return mappedClass;
            }
        }

        LOGGER.warning("No specific test mapping found for scenario " + scenario.getScenarioId() + ". Defaulting to BaseTest.");
        return testRegistry.getTestClass("default_fallback");
    }

    private String buildSearchContext(GeneratedScenario scenario) {
        StringBuilder context = new StringBuilder();
        if (scenario.getScenarioName() != null) {
            context.append(scenario.getScenarioName().toLowerCase()).append(" ");
        }
        if (scenario.getDescription() != null) {
            context.append(scenario.getDescription().toLowerCase()).append(" ");
        }
        if (scenario.getSteps() != null) {
            for (GeneratedStep step : scenario.getSteps()) {
                if (step.getAction() != null) {
                    context.append(step.getAction().toLowerCase()).append(" ");
                }
            }
        }
        return context.toString();
    }
}