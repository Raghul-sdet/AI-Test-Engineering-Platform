package com.enterprise.banking.ai.exploration.report;

import java.util.logging.Logger;

/**
 * Exporter responsible for generating human-readable representations of exploration data.
 */
public class ExplorationDashboardExporter {
    private static final Logger LOGGER = Logger.getLogger(ExplorationDashboardExporter.class.getName());

    public String exportToJson(ExplorationReport report) {
        LOGGER.info("Exporting AI Exploration report to JSON.");
        return "{\n" +
               "  \"status\": \"" + report.getFinalState().name() + "\",\n" +
               "  \"nodesDiscovered\": " + report.getStatistics().getTotalNodesDiscovered() + ",\n" +
               "  \"estimatedCoverage\": " + report.getStatistics().getEstimatedCoverage() + "\n" +
               "}";
    }
}