package com.enterprise.banking.ai.generator;

import com.enterprise.banking.ai.model.GeneratedScenario;
import com.enterprise.banking.ai.model.ScenarioCategory;
import com.enterprise.banking.ai.model.ScenarioPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Intelligent rule engine that parses requirement text and dynamically generates
 * appropriate test scenarios based on domain-specific keywords and business rules.
 */
public class ScenarioRuleEngine {

    private static final Logger LOGGER = Logger.getLogger(ScenarioRuleEngine.class.getName());

    /**
     * Analyzes the context of a requirement or feature and applies business rules
     * to generate a tailored list of scenario frameworks.
     *
     * @param contextText The descriptive text of the requirement or feature.
     * @return A list of GeneratedScenario objects configured with appropriate categories and priorities.
     */
    public List<GeneratedScenario> applyRules(String contextText) {
        if (contextText == null || contextText.trim().isEmpty()) {
            LOGGER.warning("Empty context provided to ScenarioRuleEngine. Returning baseline scenarios only.");
            return generateBaselineScenarios();
        }

        List<GeneratedScenario> scenarios = new ArrayList<>(generateBaselineScenarios());
        String normalizedContext = contextText.toLowerCase(Locale.ROOT);

        evaluateFinancialRules(normalizedContext, scenarios);
        evaluateIdentityRules(normalizedContext, scenarios);
        evaluateSystemRules(normalizedContext, scenarios);
        evaluateGeneralRules(normalizedContext, scenarios);

        LOGGER.log(Level.INFO, "Rule engine applied successfully. Generated {0} scenarios.", scenarios.size());
        return scenarios;
    }

    /**
     * Always generate at least one positive and one regression scenario for any feature.
     */
    private List<GeneratedScenario> generateBaselineScenarios() {
        List<GeneratedScenario> baseline = new ArrayList<>();
        
        GeneratedScenario positive = new GeneratedScenario();
        positive.setScenarioName("Verify standard positive workflow execution");
        positive.setCategory(ScenarioCategory.POSITIVE);
        positive.setPriority(ScenarioPriority.HIGH);
        positive.setDescription("Ensure the primary path executes without errors.");
        baseline.add(positive);

        GeneratedScenario regression = new GeneratedScenario();
        regression.setScenarioName("Verify backward compatibility and legacy regression");
        regression.setCategory(ScenarioCategory.REGRESSION);
        regression.setPriority(ScenarioPriority.MEDIUM);
        regression.setDescription("Validate existing functionalities remain unaffected.");
        baseline.add(regression);

        return baseline;
    }

    private void evaluateFinancialRules(String context, List<GeneratedScenario> scenarios) {
        if (context.contains("payment") || context.contains("transfer") || context.contains("transaction")) {
            LOGGER.fine("Financial keywords detected. Applying financial scenario rules.");
            
            GeneratedScenario financialNegative = new GeneratedScenario();
            financialNegative.setScenarioName("Verify transaction failure on insufficient funds or invalid accounts");
            financialNegative.setCategory(ScenarioCategory.NEGATIVE);
            financialNegative.setPriority(ScenarioPriority.CRITICAL);
            financialNegative.setDescription("Financial transactions must fail securely on invalid state.");
            scenarios.add(financialNegative);

            GeneratedScenario financialBoundary = new GeneratedScenario();
            financialBoundary.setScenarioName("Verify transaction limits and boundary amounts");
            financialBoundary.setCategory(ScenarioCategory.BOUNDARY);
            financialBoundary.setPriority(ScenarioPriority.HIGH);
            financialBoundary.setDescription("Test maximum and minimum transfer/payment limits.");
            scenarios.add(financialBoundary);
        }
    }

    private void evaluateIdentityRules(String context, List<GeneratedScenario> scenarios) {
        if (context.contains("authentication") || context.contains("registration") || 
            context.contains("account") || context.contains("profile")) {
            LOGGER.fine("Identity/Account keywords detected. Applying identity scenario rules.");
            
            GeneratedScenario authSecurity = new GeneratedScenario();
            authSecurity.setScenarioName("Verify secure access and session token validation");
            authSecurity.setCategory(ScenarioCategory.SECURITY);
            authSecurity.setPriority(ScenarioPriority.CRITICAL);
            authSecurity.setDescription("Ensure unauthorized access is strictly prevented.");
            scenarios.add(authSecurity);

            GeneratedScenario authNegative = new GeneratedScenario();
            authNegative.setScenarioName("Verify login/registration failure with invalid credentials");
            authNegative.setCategory(ScenarioCategory.NEGATIVE);
            authNegative.setPriority(ScenarioPriority.HIGH);
            authNegative.setDescription("System must reject invalid authentication attempts securely.");
            scenarios.add(authNegative);
        }
    }

    private void evaluateSystemRules(String context, List<GeneratedScenario> scenarios) {
        if (context.contains("security")) {
            GeneratedScenario securityCheck = new GeneratedScenario();
            securityCheck.setScenarioName("Verify OWASP vulnerabilities and data encryption");
            securityCheck.setCategory(ScenarioCategory.SECURITY);
            securityCheck.setPriority(ScenarioPriority.CRITICAL);
            securityCheck.setDescription("Validate encryption in transit and at rest.");
            scenarios.add(securityCheck);
        }

        if (context.contains("performance")) {
            GeneratedScenario performanceCheck = new GeneratedScenario();
            performanceCheck.setScenarioName("Verify system load limits and concurrent user scaling");
            performanceCheck.setCategory(ScenarioCategory.PERFORMANCE);
            performanceCheck.setPriority(ScenarioPriority.HIGH);
            performanceCheck.setDescription("Ensure the system maintains response times under defined load.");
            scenarios.add(performanceCheck);
        }
    }

    private void evaluateGeneralRules(String context, List<GeneratedScenario> scenarios) {
        if (context.contains("search")) {
            GeneratedScenario searchBoundary = new GeneratedScenario();
            searchBoundary.setScenarioName("Verify search behavior with empty, large, and special character payloads");
            searchBoundary.setCategory(ScenarioCategory.BOUNDARY);
            searchBoundary.setPriority(ScenarioPriority.MEDIUM);
            searchBoundary.setDescription("Validate search algorithm resilience.");
            scenarios.add(searchBoundary);
        }
    }
}