package com.enterprise.banking.ai.generator;

import com.enterprise.banking.ai.model.GeneratedScenario;
import com.enterprise.banking.ai.model.GeneratedStep;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides reusable, standardized test step templates based on scenario categorization.
 * This engine ensures consistent step structures (Arrange, Act, Assert) across all generated tests.
 */
public class ScenarioTemplateEngine {

    private static final Logger LOGGER = Logger.getLogger(ScenarioTemplateEngine.class.getName());

    /**
     * Populates the provided scenario with standardized execution steps based on its assigned category.
     *
     * @param scenario The generated scenario needing test steps
     */
    public void applyTemplate(GeneratedScenario scenario) {
        if (scenario == null || scenario.getCategory() == null) {
            LOGGER.warning("Invalid scenario or missing category. Cannot apply templates.");
            return;
        }

        LOGGER.log(Level.FINE, "Applying step templates for scenario: {0} [{1}]", 
                new Object[]{scenario.getScenarioId(), scenario.getCategory()});

        switch (scenario.getCategory()) {
            case POSITIVE -> applyPositiveTemplate(scenario);
            case NEGATIVE -> applyNegativeTemplate(scenario);
            case BOUNDARY -> applyBoundaryTemplate(scenario);
            case SECURITY -> applySecurityTemplate(scenario);
            case REGRESSION -> applyRegressionTemplate(scenario);
            case PERFORMANCE -> applyPerformanceTemplate(scenario);
            default -> applyGenericTemplate(scenario);
        }
    }

    private void applyPositiveTemplate(GeneratedScenario scenario) {
        scenario.addStep(new GeneratedStep(1, "Initialize system state with valid test data.", "System is ready and baseline state is confirmed."));
        scenario.addStep(new GeneratedStep(2, "Navigate to the designated application module.", "Target module loads successfully without errors."));
        scenario.addStep(new GeneratedStep(3, "Input valid business payload and submit.", "Application accepts the payload and triggers processing."));
        scenario.addStep(new GeneratedStep(4, "Verify downstream system state and success messages.", "Success message is displayed and data is persisted accurately."));
        scenario.setExpectedResult("The primary workflow completes successfully, meeting all business requirements.");
    }

    private void applyNegativeTemplate(GeneratedScenario scenario) {
        scenario.addStep(new GeneratedStep(1, "Initialize system state with invalid or incomplete test data.", "System is ready and baseline state is confirmed."));
        scenario.addStep(new GeneratedStep(2, "Navigate to the designated application module.", "Target module loads successfully."));
        scenario.addStep(new GeneratedStep(3, "Submit the invalid payload bypassing client-side validation if possible.", "Submission attempt is made."));
        scenario.addStep(new GeneratedStep(4, "Verify appropriate error handling and validation messages.", "System gracefully rejects the payload and displays a localized error message."));
        scenario.setExpectedResult("The system rejects invalid input and prevents data corruption or unhandled exceptions.");
    }

    private void applyBoundaryTemplate(GeneratedScenario scenario) {
        scenario.addStep(new GeneratedStep(1, "Identify maximum/minimum boundary values for the target fields.", "Boundary limits are calculated."));
        scenario.addStep(new GeneratedStep(2, "Input data exactly at the boundary limit and submit.", "System processes the edge-case data."));
        scenario.addStep(new GeneratedStep(3, "Input data slightly outside the boundary limit and submit.", "System processes the out-of-bounds data."));
        scenario.addStep(new GeneratedStep(4, "Validate acceptance of boundary limits and rejection of out-of-bounds data.", "Application strictly adheres to defined threshold limits."));
        scenario.setExpectedResult("The system accurately enforces numeric, length, or transactional boundaries.");
    }

    private void applySecurityTemplate(GeneratedScenario scenario) {
        scenario.addStep(new GeneratedStep(1, "Establish an unauthenticated or unauthorized user session.", "Session token is captured or absent."));
        scenario.addStep(new GeneratedStep(2, "Attempt to access protected resources or endpoints.", "Request is dispatched to the server."));
        scenario.addStep(new GeneratedStep(3, "Inject unauthorized payload (e.g., SQLi, XSS strings).", "Payload is transmitted."));
        scenario.addStep(new GeneratedStep(4, "Verify strict access denial and security logging.", "HTTP 401/403 is returned, and intrusion attempt is logged."));
        scenario.setExpectedResult("System blocks unauthorized access and remains resilient against common injection attacks.");
    }

    private void applyRegressionTemplate(GeneratedScenario scenario) {
        scenario.addStep(new GeneratedStep(1, "Identify legacy workflow overlapping with new requirement.", "Target legacy module is identified."));
        scenario.addStep(new GeneratedStep(2, "Execute standard legacy business process.", "Legacy process executes seamlessly."));
        scenario.addStep(new GeneratedStep(3, "Verify backward compatibility of data formats.", "Data remains consistent across old and new module versions."));
        scenario.setExpectedResult("New code deployment does not negatively impact existing functionality.");
    }

    private void applyPerformanceTemplate(GeneratedScenario scenario) {
        scenario.addStep(new GeneratedStep(1, "Configure load generator for target endpoints.", "Virtual users and concurrency limits are set."));
        scenario.addStep(new GeneratedStep(2, "Execute sustained load over a specified duration.", "System is subjected to continuous requests."));
        scenario.addStep(new GeneratedStep(3, "Monitor response times, CPU, and memory utilization.", "Metrics are captured under load."));
        scenario.setExpectedResult("System response time remains under the defined SLA without crashing.");
    }

    private void applyGenericTemplate(GeneratedScenario scenario) {
        scenario.addStep(new GeneratedStep(1, "Prepare test environment.", "Environment is clean."));
        scenario.addStep(new GeneratedStep(2, "Execute defined action.", "Action completes."));
        scenario.addStep(new GeneratedStep(3, "Verify expected state.", "State matches expectations."));
        scenario.setExpectedResult("The scenario executes as requested.");
    }
}