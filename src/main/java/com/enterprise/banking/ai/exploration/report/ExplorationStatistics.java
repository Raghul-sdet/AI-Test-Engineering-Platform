package com.enterprise.banking.ai.exploration.report;

/**
 * Aggregated analytics for the exploration session lifecycle.
 */
public class ExplorationStatistics {
    private final int totalNodesDiscovered;
    private final int totalActionsExecuted;
    private final int totalAnomaliesDetected;
    private final double estimatedCoverage;

    public ExplorationStatistics(int totalNodesDiscovered, int totalActionsExecuted, int totalAnomaliesDetected, double estimatedCoverage) {
        this.totalNodesDiscovered = totalNodesDiscovered;
        this.totalActionsExecuted = totalActionsExecuted;
        this.totalAnomaliesDetected = totalAnomaliesDetected;
        this.estimatedCoverage = estimatedCoverage;
    }

    public int getTotalNodesDiscovered() { return totalNodesDiscovered; }
    public int getTotalActionsExecuted() { return totalActionsExecuted; }
    public int getTotalAnomaliesDetected() { return totalAnomaliesDetected; }
    public double getEstimatedCoverage() { return estimatedCoverage; }
}