package com.enterprise.banking.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Standalone, single-method TestNG class used purely as a safe dynamic-execution
 * target for {@link DynamicExecutionTest}.
 * <p>
 * Previously {@code DynamicExecutionTest} mapped its "valid" ExecutionPlan to
 * {@code this.getClass().getName()} (i.e. itself). Since {@code RuntimeExecutionEngine}
 * runs the ENTIRE mapped class through a fresh, nested TestNG instance, that made the
 * dynamic run re-execute {@code testDynamicOrchestratorPipeline} recursively inside
 * itself - which then failed its own nested assertions and dragged the outer run's
 * failure count above zero, marking the "valid" plan FAILED instead of COMPLETED.
 * <p>
 * This class has no relationship to the orchestrator, so it can be executed
 * dynamically without any risk of recursion.
 */
public class DynamicExecutionDummyTarget {

    @Test
    public void dummyExecutionTarget() {
        Assert.assertTrue(true, "Programmatic dynamic execution successful.");
    }
}
