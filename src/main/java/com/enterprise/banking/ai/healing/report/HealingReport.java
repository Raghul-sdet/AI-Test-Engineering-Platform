package com.enterprise.banking.ai.healing.report;

import com.enterprise.banking.ai.healing.model.HealingResult;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Formal structured report generated at the end of an execution suite detailing
 * all dynamic runtime interventions made by the AI.
 */
public class HealingReport {

    private String reportId;
    private LocalDateTime generationTime;
    private HealingMetrics metrics;
    private List<HealingResult> detailedInterventions;

    public HealingReport() {
        this.reportId = "HEAL-REP-" + System.currentTimeMillis();
        this.generationTime = LocalDateTime.now();
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public LocalDateTime getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(LocalDateTime generationTime) {
        this.generationTime = generationTime;
    }

    public HealingMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(HealingMetrics metrics) {
        this.metrics = metrics;
    }

    public List<HealingResult> getDetailedInterventions() {
        return detailedInterventions;
    }

    public void setDetailedInterventions(List<HealingResult> detailedInterventions) {
        this.detailedInterventions = detailedInterventions;
    }
}