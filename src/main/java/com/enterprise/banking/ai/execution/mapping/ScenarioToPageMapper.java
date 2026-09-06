package com.enterprise.banking.ai.execution.mapping;

import com.enterprise.banking.ai.execution.registry.PageObjectRegistry;
import com.enterprise.banking.ai.model.GeneratedScenario;
import com.enterprise.banking.ai.model.GeneratedStep;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Intelligent mapper that scans a GeneratedScenario to locate the most appropriate
 * existing Selenium Page Object in the framework.
 */
public class ScenarioToPageMapper implements ExecutionMapper {

    private static final Logger LOGGER = Logger.getLogger(ScenarioToPageMapper.class.getName());
    private final PageObjectRegistry pageRegistry;

    /**
     * Constructor initializing the required registry.
     *
     * @param pageRegistry The page object registry containing available framework classes.
     */
    public ScenarioToPageMapper(PageObjectRegistry pageRegistry) {
        if (pageRegistry == null) {
            throw new IllegalArgumentException("PageObjectRegistry cannot be null.");
        }
        this.pageRegistry = pageRegistry;
    }

    @Override
    public String mapScenario(GeneratedScenario scenario) {
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario cannot be null for page mapping.");
        }

        String searchContext = buildSearchContext(scenario);
        
        for (String keyword : pageRegistry.getRegisteredKeywords()) {
            if (searchContext.contains(keyword)) {
                String mappedPage = pageRegistry.getPageObject(keyword);
                LOGGER.log(Level.FINE, "Mapped scenario {0} to Page Object: {1} via keyword [{2}]",
                        new Object[]{scenario.getScenarioId(), mappedPage, keyword});
                return mappedPage;
            }
        }

        LOGGER.warning("No specific page mapping found for scenario " + scenario.getScenarioId() + ". Defaulting to BasePage.");
        return pageRegistry.getPageObject("default_fallback");
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