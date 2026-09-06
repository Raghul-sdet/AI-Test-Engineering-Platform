package com.enterprise.banking.ai.healing.service;

import com.enterprise.banking.ai.healing.engine.SelfHealingEngine;
import com.enterprise.banking.ai.healing.model.HealingAction;
import com.enterprise.banking.ai.healing.model.HealingResult;
import com.enterprise.banking.ai.healing.report.HealingMetrics;
import com.enterprise.banking.ai.healing.report.HealingReport;
import com.enterprise.banking.ai.healing.runtime.HealingExecutionContext;
import com.enterprise.banking.ai.healing.runtime.HealingRetryExecutor;
import com.enterprise.banking.ai.healing.strategy.DomHealingStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Main public facade orchestrating AI self-healing interceptions, data tracking,
 * and final metric report generation.
 */
public class SelfHealingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelfHealingService.class);

    private final HealingRetryExecutor retryExecutor;
    private final HealingHistoryService historyService;

    public SelfHealingService() {
        SelfHealingEngine engine = new SelfHealingEngine();
        DomHealingStrategy domStrategy = new DomHealingStrategy(engine);
        
        this.retryExecutor = new HealingRetryExecutor(domStrategy);
        this.historyService = new HealingHistoryService();
        LOGGER.info("Enterprise AI Self-Healing Service Initialized.");
    }

    /**
     * Entry point for executing automation actions wrapped in the healing proxy.
     *
     * @param action          The lambda logic to execute
     * @param originalLocator The hardcoded page object locator
     * @param historicDom     The last known good DOM state of the element
     */
    public void executeAction(Runnable action, String originalLocator, String historicDom) {
        HealingResult result = retryExecutor.executeWithHealing(action, originalLocator, historicDom);
        if (result != null) {
            historyService.recordResult(result);
        }
    }

    /**
     * Configures the thread-local execution boundary for a new test suite run.
     *
     * @param executionId Unique ID for the test run
     */
    public void startExecutionBoundary(String executionId) {
        HealingExecutionContext.clear();
        HealingExecutionContext.setExecutionId(executionId);
        LOGGER.info("Healing Execution Boundary started: {}", executionId);
    }

    /**
     * Generates a comprehensive summary report of all healing interventions.
     *
     * @return Populated HealingReport
     */
    public HealingReport generateFinalReport() {
        LOGGER.info("Generating Final Self-Healing Audit Report.");
        HealingReport report = new HealingReport();
        List<HealingResult> history = historyService.getAllResults();
        
        report.setDetailedInterventions(history);
        report.setMetrics(calculateMetrics(history));
        
        return report;
    }

    private HealingMetrics calculateMetrics(List<HealingResult> results) {
        HealingMetrics metrics = new HealingMetrics();
        if (results == null || results.isEmpty()) {
            return metrics;
        }

        int total = results.size();
        int success = 0;
        int locatorHealed = 0;
        long totalTime = 0;

        for (HealingResult res : results) {
            totalTime += res.getExecutionTimeMillis();
            if (res.isSuccessful()) {
                success++;
                if (res.getActionTaken() == HealingAction.LOCATOR_REPLACED) {
                    locatorHealed++;
                }
            }
        }

        metrics.setTotalHealedElements(locatorHealed);
        metrics.setHealingSuccessRate((double) success / total * 100.0);
        metrics.setLocatorRecoveryPercentage((double) locatorHealed / total * 100.0);
        metrics.setAverageHealingTimeMillis(totalTime / (double) total);

        return metrics;
    }
}