package com.enterprise.banking.ai.exploration.navigation;

import com.enterprise.banking.ai.exploration.ExplorationStrategy;
import java.util.logging.Logger;

/**
 * Determines the next logical navigation target based on the active exploration strategy.
 */
public class NavigationPlanner {
    private static final Logger LOGGER = Logger.getLogger(NavigationPlanner.class.getName());
    private final NavigationTracker tracker;

    public NavigationPlanner(NavigationTracker tracker) {
        this.tracker = tracker;
    }

    public boolean shouldExploreNode(NavigationNode node, ExplorationStrategy strategy) {
        if (tracker.getHistory().hasVisited(node)) {
            LOGGER.info("Node previously visited. NavigationPlanner rejecting duplicate path.");
            return false;
        }
        return true;
    }
}