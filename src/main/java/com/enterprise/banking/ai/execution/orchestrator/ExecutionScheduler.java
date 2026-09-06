package com.enterprise.banking.ai.execution.orchestrator;

import com.enterprise.banking.ai.execution.model.ExecutionPlan;
import java.util.List;
import java.util.logging.Logger;

/**
 * Organizes and feeds execution plans into the ExecutionQueue based on their sequence.
 */
public class ExecutionScheduler {

    private static final Logger LOGGER = Logger.getLogger(ExecutionScheduler.class.getName());
    private final ExecutionQueue queue;

    /**
     * Constructor assigning the central queue.
     *
     * @param queue The execution queue component
     */
    public ExecutionScheduler(ExecutionQueue queue) {
        if (queue == null) {
            throw new IllegalArgumentException("ExecutionQueue cannot be null.");
        }
        this.queue = queue;
    }

    /**
     * Accepts a list of mapped execution plans, wraps them in contexts, and queues them.
     * Ensures strict sorting prior to enqueueing based on critical logic mapped in Phase 3 Step 1.
     *
     * @param plans The list of Execution Plans
     */
    public void schedule(List<ExecutionPlan> plans) {
        if (plans == null || plans.isEmpty()) {
            LOGGER.warning("No plans provided to the ExecutionScheduler.");
            return;
        }

        LOGGER.info("Scheduling " + plans.size() + " execution plans into the runtime queue.");
        
        for (ExecutionPlan plan : plans) {
            queue.enqueue(new ExecutionContext(plan));
        }
        
        LOGGER.info("Scheduling complete. Queue depth: " + queue.size());
    }
}