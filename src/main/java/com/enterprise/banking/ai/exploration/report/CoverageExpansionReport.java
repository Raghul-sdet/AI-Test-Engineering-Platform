package com.enterprise.banking.ai.exploration.report;

import java.util.List;

/**
 * Report detailing previously unknown paths identified by the exploration engine.
 */
public class CoverageExpansionReport {
    private final List<String> newlyDiscoveredPaths;

    public CoverageExpansionReport(List<String> newlyDiscoveredPaths) {
        this.newlyDiscoveredPaths = newlyDiscoveredPaths;
    }

    public List<String> getNewlyDiscoveredPaths() { return newlyDiscoveredPaths; }
}