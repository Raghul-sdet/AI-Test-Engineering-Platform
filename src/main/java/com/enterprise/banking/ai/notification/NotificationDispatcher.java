package com.enterprise.banking.ai.notification;

import com.enterprise.banking.ai.notification.scheduler.NotificationQueue;
import com.enterprise.banking.ai.notification.scheduler.NotificationScheduler;
import com.enterprise.banking.ai.notification.scheduler.ScheduledNotification;
import java.util.List;
import java.util.logging.Logger;

/**
 * Acts as the bridge between the NotificationManager and the asynchronous Queue system.
 */
public class NotificationDispatcher {
    private static final Logger LOGGER = Logger.getLogger(NotificationDispatcher.class.getName());
    
    private final NotificationQueue queue;
    private final NotificationScheduler scheduler;
    private final NotificationHistory history;

    public NotificationDispatcher(NotificationHistory history) {
        this.queue = new NotificationQueue();
        this.scheduler = new NotificationScheduler(this.queue);
        this.history = history;
    }

    public void dispatchAsync(NotificationContext context, NotificationTemplate template) {
        LOGGER.info("Enqueuing notification: " + template.getTitle());
        ScheduledNotification job = new ScheduledNotification(context, template);
        queue.enqueue(job);
    }

    public void processQueue(List<NotificationChannel> activeChannels) {
        if (activeChannels.isEmpty()) {
            LOGGER.warning("Queue processing blocked: No active Notification Channels.");
            return;
        }
        
        while (!queue.isEmpty()) {
            // Dequeue ONE job, then fan it out to EVERY active channel (true multicast).
            // Previously each channel independently called scheduler.processNext(channel),
            // which dequeued from the same shared queue - so with fewer queued jobs than
            // channels, only the first channel(s) in the loop ever got a job and the rest
            // silently got null once the queue drained.
            ScheduledNotification job = queue.dequeue();
            if (job == null) {
                break;
            }
            for (NotificationChannel channel : activeChannels) {
                NotificationResult result = scheduler.deliver(job, channel);
                if (result != null) {
                    history.logResult(result);
                }
            }
        }
    }
}