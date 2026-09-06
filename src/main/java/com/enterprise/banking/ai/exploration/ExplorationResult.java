package com.enterprise.banking.ai.exploration;

import com.enterprise.banking.ai.exploration.report.ExplorationReport;

/**
 * Final execution wrapper encapsulating the session data.
 */
public class ExplorationResult {
    private final ExplorationSession session;
    private final ExplorationReport report;

    public ExplorationResult(ExplorationSession session, ExplorationReport report) {
        this.session = session;
        this.report = report;
    }

    public ExplorationSession getSession() { return session; }
    public ExplorationReport getReport() { return report; }
}