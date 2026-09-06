package com.enterprise.banking.ai.exploration.learning;

import com.enterprise.banking.ai.exploration.navigation.NavigationGraph;

/**
 * Evaluates the depth and breadth of the NavigationGraph to quantify coverage boundaries.
 */
public class CoverageLearner {

    public double calculateExplorationCoverage(NavigationGraph graph) {
        // Simplified metric: Extrapolation of nodes visited against estimated application depth
        int nodes = graph.getNodes().size();
        return Math.min(100.0, nodes * 5.5); // Mock algorithm
    }
}