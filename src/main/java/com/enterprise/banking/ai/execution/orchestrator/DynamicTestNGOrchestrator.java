package com.enterprise.banking.ai.execution.orchestrator;

import com.enterprise.banking.ai.execution.model.ExecutionPlan;
import com.enterprise.banking.ai.execution.model.ExecutionSummary;
import com.enterprise.banking.ai.execution.runtime.RuntimeExecutionEngine;

import java.util.List;
import java.util.logging.Logger;

/**
 * Core orchestrator that manages the execution queue, delegates to workers, 
 * and controls the holistic dynamic TestNG pipeline.
 */
public class DynamicTestNGOrchestrator {

    private static final Logger LOGGER = Logger.getLogger(DynamicTestNGOrchestrator.class.getName());

    private final ExecutionQueue queue;
    private final ExecutionScheduler scheduler;
    private final RuntimeExecutionEngine runtimeEngine;
    private final ExecutionResultCollector resultCollector;

    /**
     * Initializes the complete orchestration architecture.
     */
    public DynamicTestNGOrchestrator() {
        this.queue = new ExecutionQueue();
        this.scheduler = new ExecutionScheduler(queue);
        this.runtimeEngine = new RuntimeExecutionEngine();
        this.resultCollector = new ExecutionResultCollector();
    }

    /**
     * Accepts a prioritized list of execution plans and processes them dynamically
     * via TestNG, blocking until complete.
     *
     * @param plans The ordered list of mapped Execution Plans
     * @return ExecutionSummary containing aggregated execution data
     */
    public ExecutionSummary orchestrate(List<ExecutionPlan> plans) {
        if (plans == null || plans.isEmpty()) {
            LOGGER.warning("DynamicTestNGOrchestrator received an empty plan list.");
            return resultCollector.buildSummary();
        }

        LOGGER.info("--- AI Orchestration Pipeline Commencing ---");

        // 1. Schedule items into queue
        scheduler.schedule(plans);

        // 2. Instantiate worker (Can be easily extended to ThreadPool for parallel execution)
        ExecutionWorker worker = new ExecutionWorker(queue, runtimeEngine, resultCollector);
        
        // 3. Execute synchronous processing for stable TestNG context
        worker.run();

        LOGGER.info("--- AI Orchestration Pipeline Concluded ---");
        
        // 4. Return generated metrics and summaries
        return resultCollector.buildSummary();
    }
    
    /**
     * Exposes result collector for advanced metric retrieval.
     * @return The populated ExecutionResultCollector
     */
    public ExecutionResultCollector getResultCollector() {
        return this.resultCollector;
    }
}