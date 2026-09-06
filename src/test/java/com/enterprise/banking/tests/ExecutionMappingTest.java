package com.enterprise.banking.tests;

import com.enterprise.banking.ai.execution.model.ExecutionPlan;
import com.enterprise.banking.ai.execution.model.ExecutionStatus;
import com.enterprise.banking.ai.execution.model.ExecutionTarget;
import com.enterprise.banking.ai.execution.service.ExecutionMappingService;
import com.enterprise.banking.ai.model.GeneratedScenario;
import com.enterprise.banking.ai.model.GeneratedStep;
import com.enterprise.banking.ai.model.ScenarioCategory;
import com.enterprise.banking.ai.model.ScenarioCollection;
import com.enterprise.banking.ai.model.ScenarioPriority;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Unit test validating the AI Smart Execution Mapper pipeline.
 * Ensures generated scenarios correctly map to existing TestNG and Selenium classes,
 * construct proper task plans, and sort by execution priority.
 */
public class ExecutionMappingTest {

    private ExecutionMappingService mappingService;

    @BeforeMethod
    public void setUp() {
        mappingService = new ExecutionMappingService();
    }

    @Test
    public void testSuccessfulExecutionPlanMappingAndPrioritization() {
        // Arrange: Construct Mock Scenarios targeting specific keywords
        ScenarioCollection collection = new ScenarioCollection("REQ-MOCK-123");

        // Scenario 1: Low Priority Profile Check
        GeneratedScenario profileScenario = new GeneratedScenario();
        profileScenario.setScenarioName("Verify customer profile update");
        profileScenario.setDescription("Customer accesses account profile to update email.");
        profileScenario.setPriority(ScenarioPriority.LOW);
        profileScenario.setCategory(ScenarioCategory.POSITIVE);
        profileScenario.addStep(new GeneratedStep(1, "Navigate to profile", "Loaded"));
        collection.addScenario(profileScenario);

        // Scenario 2: Critical Priority Payment Transfer
        GeneratedScenario paymentScenario = new GeneratedScenario();
        paymentScenario.setScenarioName("Verify critical international payment transfer");
        paymentScenario.setDescription("Execute a cross border transfer.");
        paymentScenario.setPriority(ScenarioPriority.CRITICAL);
        paymentScenario.setCategory(ScenarioCategory.NEGATIVE);
        paymentScenario.addStep(new GeneratedStep(1, "Input payment details", "Accepted"));
        paymentScenario.addStep(new GeneratedStep(2, "Submit transfer", "Processed"));
        collection.addScenario(paymentScenario);

        // Act: Map Scenarios to Execution Plans
        List<ExecutionPlan> generatedPlans = mappingService.mapScenariosToExecutionPlans(collection);

        // Assert: Validation of Pipeline
        Assert.assertNotNull(generatedPlans, "Generated plans should not be null");
        Assert.assertEquals(generatedPlans.size(), 2, "Should generate exactly 2 execution plans");

        // Validate Priority Ordering (CRITICAL should be index 0)
        ExecutionPlan highestPriorityPlan = generatedPlans.get(0);
        Assert.assertEquals(highestPriorityPlan.getPriority(), ScenarioPriority.CRITICAL, "Critical priority plan should be first in order");
        Assert.assertEquals(highestPriorityPlan.getExecutionOrder(), 1, "Execution order should be properly assigned starting at 1");
        
        // Validate Keyword Mapping to Existing Framework Assets
        Assert.assertEquals(highestPriorityPlan.getMappedTestClass(), "com.enterprise.banking.tests.TransferFundsTest", "Payment keyword should map to TransferFundsTest");
        Assert.assertEquals(highestPriorityPlan.getMappedPageObject(), "com.enterprise.banking.pages.TransferFundsPage", "Payment keyword should map to TransferFundsPage");

        ExecutionPlan lowestPriorityPlan = generatedPlans.get(1);
        Assert.assertEquals(lowestPriorityPlan.getPriority(), ScenarioPriority.LOW, "Low priority plan should be second");
        Assert.assertEquals(lowestPriorityPlan.getMappedTestClass(), "com.enterprise.banking.tests.AccountOverviewTest", "Customer/Profile keyword should map to AccountOverviewTest");

        // Validate Plan and Task Construction
        for (ExecutionPlan plan : generatedPlans) {
            Assert.assertNotNull(plan.getPlanId(), "Plan ID must be generated");
            Assert.assertEquals(plan.getStatus(), ExecutionStatus.PENDING, "New plans should default to PENDING status");
            Assert.assertNotNull(plan.getTasks(), "Task list should not be null");
            Assert.assertTrue(plan.getTasks().size() > 0, "Tasks should be built from steps");
            
            // Validate first task is always Page Object init
            Assert.assertEquals(plan.getTasks().get(0).getTargetType(), ExecutionTarget.PAGE_OBJECT, "First task should target Page Object initialization");
            Assert.assertEquals(plan.getTasks().get(0).getStatus(), ExecutionStatus.PENDING, "Tasks should default to PENDING");
        }
    }
}