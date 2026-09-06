package com.enterprise.banking.tests;

import com.enterprise.banking.ai.exploration.ExplorationContext;
import com.enterprise.banking.ai.exploration.ExplorationEngine;
import com.enterprise.banking.ai.exploration.ExplorationResult;
import com.enterprise.banking.ai.exploration.ExplorationState;
import com.enterprise.banking.ai.exploration.ExplorationStrategy;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Validates the End-to-End autonomous exploration lifecycle and report generation.
 */
public class ExplorationEngineTest {

    @Test
    public void testAutonomousExplorationExecution() {
        ExplorationEngine engine = new ExplorationEngine();
        ExplorationContext context = new ExplorationContext("https://enterprise.bank.local", 5, 10, ExplorationStrategy.BREADTH_FIRST);

        ExplorationResult result = engine.runExploration(context);

        Assert.assertNotNull(result, "Engine must return an ExplorationResult.");
        Assert.assertEquals(result.getSession().getState(), ExplorationState.COMPLETED, "Exploration must complete without failure on valid mock DOMs.");
        
        Assert.assertEquals(result.getReport().getStatistics().getTotalNodesDiscovered(), 5, "Graph must map exactly 5 nodes for depth 5 traversal.");
        Assert.assertTrue(result.getReport().getStatistics().getEstimatedCoverage() > 0, "Engine must calculate test coverage expansion metrics.");
        Assert.assertFalse(result.getReport().getCoverageReport().getNewlyDiscoveredPaths().isEmpty(), "Engine must generate a coverage expansion report.");
    }
}