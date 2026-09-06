package com.enterprise.banking.tests;

import com.enterprise.banking.ai.exploration.ExplorationStrategy;
import com.enterprise.banking.ai.exploration.navigation.NavigationNode;
import com.enterprise.banking.ai.exploration.navigation.NavigationPlanner;
import com.enterprise.banking.ai.exploration.navigation.NavigationTracker;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Validates that the planner honors history restrictions to prevent duplicate path traversal.
 */
public class NavigationPlannerTest {

    @Test
    public void testRejectsPreviouslyVisitedNode() {
        NavigationTracker tracker = new NavigationTracker();
        NavigationNode node = new NavigationNode("url", "hash123", "Title");
        
        tracker.updateCurrentState(node, "LOAD"); // Records as visited

        NavigationPlanner planner = new NavigationPlanner(tracker);
        
        boolean shouldExplore = planner.shouldExploreNode(node, ExplorationStrategy.COVERAGE_DRIVEN);
        
        Assert.assertFalse(shouldExplore, "Planner must reject nodes that have already been historically mapped.");
    }
}