package com.enterprise.banking.ai.execution.mapping;

import com.enterprise.banking.ai.execution.model.ExecutionPlan;
import java.util.Comparator;
import java.util.List;

/**
 * Engine responsible for evaluating and assigning execution order 
 * to a suite of execution plans based on predefined business priority weights.
 */
public class ExecutionPriorityEngine {

    /**
     * Sorts the provided list of execution plans in place, prioritizing CRITICAL down to LOW,
     * and assigns numeric execution sequence orders.
     *
     * @param plans The list of execution plans to organize
     */
    public void prioritizeAndOrder(List<ExecutionPlan> plans) {
        if (plans == null || plans.isEmpty()) {
            return;
        }

        plans.sort(Comparator.comparingInt(this::calculateWeight).reversed());

        int orderSequence = 1;
        for (ExecutionPlan plan : plans) {
            plan.setExecutionOrder(orderSequence++);
        }
    }

    /**
     * Maps the enum priority to an absolute numeric weight for sorting.
     *
     * @param plan The execution plan to evaluate
     * @return Numeric weight (higher is more critical)
     */
    private int calculateWeight(ExecutionPlan plan) {
        if (plan.getPriority() == null) {
            return 0;
        }
        
        switch (plan.getPriority()) {
            case CRITICAL:
                return 40;
            case HIGH:
                return 30;
            case MEDIUM:
                return 20;
            case LOW:
                return 10;
            default:
                return 0;
        }
    }
}