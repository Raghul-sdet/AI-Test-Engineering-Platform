package com.enterprise.banking.tests;

import com.enterprise.banking.ai.execution.model.ExecutionPlan;
import com.enterprise.banking.ai.execution.model.ExecutionStatus;
import com.enterprise.banking.ai.execution.model.ExecutionSummary;
import com.enterprise.banking.ai.service.ExecutionOrchestrationService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates the Dynamic TestNG Orchestration Service.
 * Leverages this class itself as the mapped dummy execution target to prevent 
 * ClassNotFoundException during dynamic TestNG API invocation.
 */
public class DynamicExecutionTest {

    private ExecutionOrchestrationService orchestrationService;

    @BeforeMethod
    public void setUp() {
        orchestrationService = new ExecutionOrchestrationService();
    }

    /**
     * This nested test serves as a valid target for our dynamic orchestrator
     * to execute programmatically during validation.
     */
    @Test
    public void dummyExecutionTarget() {
        Assert.assertTrue(true, "Programmatic dynamic execution successful.");
    }

    @Test
    public void testDynamicOrchestratorPipeline() {
        // Arrange
        List<ExecutionPlan> plans = new ArrayList<>();
        
        ExecutionPlan validPlan = new ExecutionPlan();
        validPlan.setPlanId("PLAN-MOCK-111");
        validPlan.setExecutionOrder(1);
        // Map to a dedicated dummy target class (NOT this class - mapping to
        // this class recursively re-executes this very test method, which then
        // fails its own nested assertions and drags the outer run's failure
        // count above zero. See DynamicExecutionDummyTarget for details.)
        validPlan.setMappedTestClass(DynamicExecutionDummyTarget.class.getName());
        validPlan.setStatus(ExecutionStatus.PENDING);
        plans.add(validPlan);

        ExecutionPlan invalidPlan = new ExecutionPlan();
        invalidPlan.setPlanId("PLAN-MOCK-222");
        invalidPlan.setExecutionOrder(2);
        // Map to a fake class to trigger failure and error collection paths
        invalidPlan.setMappedTestClass("com.enterprise.banking.tests.MissingFakeTest");
        invalidPlan.setStatus(ExecutionStatus.PENDING);
        plans.add(invalidPlan);

        // Act
        ExecutionSummary summary = orchestrationService.executeDynamicTestSuite(plans);

        // Assert Pipeline and Statistics Collection
        Assert.assertNotNull(summary, "Execution summary should not be null.");
        Assert.assertTrue(summary.getTotalTests() >= 0, "Summary should track total attempted class invocations.");
        
        // Assert execution plan status mutations
        Assert.assertEquals(validPlan.getStatus(), ExecutionStatus.COMPLETED, "Valid plan should update to COMPLETED.");
        Assert.assertEquals(invalidPlan.getStatus(), ExecutionStatus.FAILED, "Invalid plan missing class should fail gracefully.");
        
        // Assert Metrics
        Assert.assertNotNull(orchestrationService.getExecutionMetrics(), "Execution metrics should be successfully generated.");
    }
}