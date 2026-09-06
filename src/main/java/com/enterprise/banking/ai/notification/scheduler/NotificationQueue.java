package com.enterprise.banking.ai.notification.scheduler;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Thread-safe, priority-sorted queue for asynchronous notification dispatch.
 */
public class NotificationQueue {
    
    private final PriorityBlockingQueue<ScheduledNotification> queue;

    public NotificationQueue() {
        this.queue = new PriorityBlockingQueue<>(100, 
            Comparator.comparing((ScheduledNotification sn) -> sn.getContext().getPriority()).reversed()
        );
    }

    public void enqueue(ScheduledNotification notification) {
        if (notification != null) {
            queue.offer(notification);
        }
    }

    public ScheduledNotification dequeue() {
        return queue.poll();
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}