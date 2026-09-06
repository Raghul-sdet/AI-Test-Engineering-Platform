package com.enterprise.banking.tests;

import com.enterprise.banking.ai.model.GeneratedScenario;
import com.enterprise.banking.ai.model.Requirement;
import com.enterprise.banking.ai.model.RequirementFeature;
import com.enterprise.banking.ai.model.RequirementPriority;
import com.enterprise.banking.ai.model.RequirementRisk;
import com.enterprise.banking.ai.model.ScenarioCategory;
import com.enterprise.banking.ai.model.ScenarioCollection;
import com.enterprise.banking.ai.model.ScenarioPriority;
import com.enterprise.banking.ai.service.ScenarioGenerationService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Validates the core logic, rule application, and template execution of the 
 * AI Test Scenario Generation Engine.
 */
public class ScenarioGenerationTest {

    private ScenarioGenerationService generationService;
    private Requirement mockRequirement;

    @BeforeMethod
    public void setUp() {
        generationService = new ScenarioGenerationService();
        mockRequirement = buildMockRequirement();
    }

    /**
     * Helper method to construct a robust mock requirement containing features 
     * and risks designed to trigger specific generation rules.
     */
    private Requirement buildMockRequirement() {
        Requirement req = new Requirement();
        req.setId("REQ-" + UUID.randomUUID().toString().substring(0, 8));
        req.setTitle("Secure Payment Transfer Enhancement");
        req.setDescription("Implement a new high-security payment transfer gateway with limit validations.");
        req.setPriority(RequirementPriority.CRITICAL);

        RequirementFeature paymentFeature = new RequirementFeature(
                "FEAT-01",
                "International Payment Transfer",
                "Payment Module",
                "User can initiate a cross-border payment transaction securely."
        );
        
        RequirementFeature authFeature = new RequirementFeature(
                "FEAT-02",
                "MFA Authentication",
                "Security Module",
                "System requires secondary authentication for high-value transfers."
        );

        RequirementRisk sqlRisk = new RequirementRisk(
                "Security Impact",
                "CRITICAL",
                "Risk of SQL injection on payment reference field.",
                "Enforce strict OWASP input validation."
        );

        req.setFeatures(Arrays.asList(paymentFeature, authFeature));
        req.setRisks(Arrays.asList(sqlRisk));

        return req;
    }

    @Test
    public void testCompleteScenarioGenerationPipeline() {
        // Execute Generation
        ScenarioCollection scenarioCollection = generationService.generateScenariosForRequirement(mockRequirement);

        // Assert Collection Validity
        Assert.assertNotNull(scenarioCollection, "Scenario collection should not be null.");
        Assert.assertEquals(scenarioCollection.getRequirementId(), mockRequirement.getId(), "Requirement IDs must match.");
        Assert.assertTrue(scenarioCollection.getTotalScenarios() > 0, "Should generate at least one scenario.");
        Assert.assertTrue(scenarioCollection.getTotalSteps() > 0, "Generated scenarios must contain test steps.");

        List<GeneratedScenario> scenarios = scenarioCollection.getScenarios();

        // Validate Scenario Categories (Rules Engine Execution)
        List<ScenarioCategory> generatedCategories = scenarios.stream()
                .filter(s -> s != null)
                .map(s -> s.getCategory())
                .collect(Collectors.toList());

        Assert.assertTrue(generatedCategories.contains(ScenarioCategory.POSITIVE), "Must contain baseline positive scenario.");
        Assert.assertTrue(generatedCategories.contains(ScenarioCategory.REGRESSION), "Must contain baseline regression scenario.");
        Assert.assertTrue(generatedCategories.contains(ScenarioCategory.NEGATIVE), "Must contain financial negative scenario due to payment keywords.");
        Assert.assertTrue(generatedCategories.contains(ScenarioCategory.SECURITY), "Must contain security scenario due to auth keywords and identified risks.");
        Assert.assertTrue(generatedCategories.contains(ScenarioCategory.BOUNDARY), "Must contain boundary scenario due to transfer limits logic.");

        // Validate Scenario Structure and Templates
        for (GeneratedScenario scenario : scenarios) {
            Assert.assertNotNull(scenario.getScenarioId(), "Scenario ID must be populated.");
            Assert.assertNotNull(scenario.getScenarioName(), "Scenario Name must be populated.");
            Assert.assertNotNull(scenario.getExpectedResult(), "Overall Expected Result must be populated via templates.");
            Assert.assertNotNull(scenario.getPriority(), "Scenario Priority must be assigned.");
            
            Assert.assertNotNull(scenario.getSteps(), "Steps collection must exist.");
            Assert.assertTrue(scenario.getSteps().size() >= 3, "Templates should generate at least 3 steps per scenario.");

            // Validate Step Structure
            scenario.getSteps().forEach(step -> {
                Assert.assertTrue(step.getStepNumber() > 0, "Step numbers must be strictly positive.");
                Assert.assertNotNull(step.getAction(), "Step action cannot be null.");
                Assert.assertNotNull(step.getExpectedResult(), "Step expected result cannot be null.");
            });
        }
        
        // Validate Priority Assignments
        boolean hasCriticalOrHigh = scenarios.stream()
                .anyMatch(s -> s.getPriority() == ScenarioPriority.CRITICAL || s.getPriority() == ScenarioPriority.HIGH);
        Assert.assertTrue(hasCriticalOrHigh, "Financial and Security features should generate Critical/High priority tests.");
    }
}