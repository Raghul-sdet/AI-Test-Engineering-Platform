package com.enterprise.banking.ai.execution.runtime;

import com.enterprise.banking.ai.execution.model.ExecutionPlan;

/**
 * Manages thread-local context data during active runtime execution, 
 * ensuring execution plans can be tracked accurately in parallel environments.
 */
public class RuntimeExecutionContext {

    private static final ThreadLocal<ExecutionPlan> CURRENT_PLAN = new ThreadLocal<>();

    /**
     * Sets the execution plan currently being executed by the current thread.
     *
     * @param plan The active execution plan
     */
    public static void setContext(ExecutionPlan plan) {
        CURRENT_PLAN.set(plan);
    }

    /**
     * Retrieves the execution plan currently bound to this thread.
     *
     * @return The active execution plan
     */
    public static ExecutionPlan getContext() {
        return CURRENT_PLAN.get();
    }

    /**
     * Clears the execution context for the current thread to prevent memory leaks.
     */
    public static void clearContext() {
        CURRENT_PLAN.remove();
    }
}