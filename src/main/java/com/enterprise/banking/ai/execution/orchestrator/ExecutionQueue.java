package com.enterprise.banking.ai.execution.orchestrator;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.Comparator;

/**
 * Thread-safe queue managing the lifecycle of prioritized execution contexts.
 */
public class ExecutionQueue {

    private final PriorityBlockingQueue<ExecutionContext> queue;

    /**
     * Initializes the priority-based execution queue using the pre-assigned execution order.
     */
    public ExecutionQueue() {
        Comparator<ExecutionContext> contextComparator = Comparator
                .comparingInt(c -> c.getExecutionPlan().getExecutionOrder());
        this.queue = new PriorityBlockingQueue<>(100, contextComparator);
    }

    /**
     * Pushes a new execution context onto the queue.
     *
     * @param context The configured execution context
     */
    public void enqueue(ExecutionContext context) {
        if (context != null) {
            queue.offer(context);
        }
    }

    /**
     * Polls the next execution context from the queue in priority order.
     *
     * @return The highest priority execution context, or null if empty
     */
    public ExecutionContext dequeue() {
        return queue.poll();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if no elements remain, false otherwise
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns the current number of pending contexts.
     *
     * @return Queue size
     */
    public int size() {
        return queue.size();
    }

    /**
     * Removes all elements from the queue.
     */
    public void clear() {
        queue.clear();
    }
} 