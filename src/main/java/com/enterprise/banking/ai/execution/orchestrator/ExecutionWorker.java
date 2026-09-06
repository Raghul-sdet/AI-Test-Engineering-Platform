package com.enterprise.banking.ai.execution.orchestrator;

import com.enterprise.banking.ai.execution.model.ExecutionPlan;
import com.enterprise.banking.ai.execution.model.ExecutionStatus;
import com.enterprise.banking.ai.execution.model.ExecutionTask;
import com.enterprise.banking.ai.execution.runtime.RuntimeExecutionContext;
import com.enterprise.banking.ai.execution.runtime.RuntimeExecutionEngine;
import com.enterprise.banking.ai.execution.runtime.RuntimeStatistics;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dedicated thread worker responsible for polling the ExecutionQueue and 
 * delegating the ExecutionPlan to the TestNG Runtime Engine.
 */
public class ExecutionWorker implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ExecutionWorker.class.getName());

    private final ExecutionQueue queue;
    private final RuntimeExecutionEngine engine;
    private final ExecutionResultCollector collector;

    /**
     * Initializes the worker with its required dependencies.
     *
     * @param queue     The shared task queue
     * @param engine    The programmatic TestNG engine
     * @param collector The shared result collector
     */
    public ExecutionWorker(ExecutionQueue queue, RuntimeExecutionEngine engine, ExecutionResultCollector collector) {
        this.queue = queue;
        this.engine = engine;
        this.collector = collector;
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            ExecutionContext context = queue.dequeue();
            if (context != null) {
                processPlan(context.getExecutionPlan());
            }
        }
    }

    private void processPlan(ExecutionPlan plan) {
        RuntimeExecutionContext.setContext(plan);
        plan.setStatus(ExecutionStatus.IN_PROGRESS);
        
        try {
            LOGGER.info("Worker initiating execution for Plan: " + plan.getPlanId());
            
            // Invoke the TestNG runner dynamically based on the mapped class
            RuntimeStatistics stats = engine.executeClass(plan.getMappedTestClass());
            
            if (stats.getFailedCount() > 0) {
                plan.setStatus(ExecutionStatus.FAILED);
            } else if (stats.getPassedCount() > 0) {
                plan.setStatus(ExecutionStatus.COMPLETED);
            } else {
                plan.setStatus(ExecutionStatus.SKIPPED);
            }

            // Sync task statuses based on class execution outcome
            for (ExecutionTask task : plan.getTasks()) {
                task.setStatus(plan.getStatus());
            }

            collector.collect(plan, stats);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Worker encountered fatal error executing plan: " + plan.getPlanId(), ex);
            plan.setStatus(ExecutionStatus.FAILED);
            for (ExecutionTask task : plan.getTasks()) {
                task.setStatus(ExecutionStatus.FAILED);
            }
        } finally {
            RuntimeExecutionContext.clearContext();
        }
    }
}