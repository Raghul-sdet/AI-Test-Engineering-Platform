package com.enterprise.banking.ai.execution.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enterprise logging utility for orchestrator execution events.
 */
public class ExecutionLogger {

    private static final Logger LOGGER = Logger.getLogger(ExecutionLogger.class.getName());

    /**
     * Logs the start of an execution plan.
     *
     * @param planId     The execution plan ID
     * @param mappedTest The target test class being executed
     */
    public static void logExecutionStart(String planId, String mappedTest) {
        LOGGER.log(Level.INFO, ">>> STARTING EXECUTION | Plan ID: {0} | Target: {1}", new Object[]{planId, mappedTest});
    }

    /**
     * Logs the successful completion of an execution plan.
     *
     * @param planId     The execution plan ID
     * @param duration   The execution duration in milliseconds
     */
    public static void logExecutionFinish(String planId, long duration) {
        LOGGER.log(Level.INFO, "<<< FINISHED EXECUTION | Plan ID: {0} | Duration: {1}ms", new Object[]{planId, duration});
    }

    /**
     * Logs a failure during execution.
     *
     * @param planId     The execution plan ID
     * @param mappedTest The target test class being executed
     * @param reason     The failure cause
     */
    public static void logExecutionFailure(String planId, String mappedTest, String reason) {
        LOGGER.log(Level.SEVERE, "!!! FAILED EXECUTION | Plan ID: {0} | Target: {1} | Reason: {2}", 
                new Object[]{planId, mappedTest, reason});
    }

    /**
     * Logs a skipped execution event.
     *
     * @param planId     The execution plan ID
     * @param mappedTest The target test class being executed
     */
    public static void logExecutionSkipped(String planId, String mappedTest) {
        LOGGER.log(Level.WARNING, "--- SKIPPED EXECUTION | Plan ID: {0} | Target: {1}", new Object[]{planId, mappedTest});
    }
}