package com.enterprise.banking.ai.exploration.report;

import com.enterprise.banking.ai.exploration.ExplorationContext;
import com.enterprise.banking.ai.exploration.ExplorationState;

/**
 * Master artifact summarizing the autonomous testing session.
 */
public class ExplorationReport {
    private final ExplorationContext context;
    private final ExplorationState finalState;
    private final ExplorationStatistics statistics;
    private final CoverageExpansionReport coverageReport;

    public ExplorationReport(ExplorationContext context, ExplorationState finalState, ExplorationStatistics statistics, CoverageExpansionReport coverageReport) {
        this.context = context;
        this.finalState = finalState;
        this.statistics = statistics;
        this.coverageReport = coverageReport;
    }

    public ExplorationContext getContext() { return context; }
    public ExplorationState getFinalState() { return finalState; }
    public ExplorationStatistics getStatistics() { return statistics; }
    public CoverageExpansionReport getCoverageReport() { return coverageReport; }
}