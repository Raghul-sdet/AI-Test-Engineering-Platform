package com.enterprise.banking.ai.notification.scheduler;

import com.enterprise.banking.ai.notification.NotificationChannel;
import com.enterprise.banking.ai.notification.NotificationResult;

/**
 * Orchestrates the extraction and delivery of notifications from the queue.
 */
public class NotificationScheduler {
    private final NotificationQueue queue;
    private final RetryNotificationJob retryJob;

    public NotificationScheduler(NotificationQueue queue) {
        this.queue = queue;
        this.retryJob = new RetryNotificationJob();
    }

    public NotificationResult processNext(NotificationChannel channel) {
        if (queue.isEmpty()) return null;

        ScheduledNotification job = queue.dequeue();
        return deliver(job, channel);
    }

    /**
     * Delivers a single already-dequeued job to a single channel, without touching the queue.
     * Extracted from {@link #processNext} so a caller (see NotificationDispatcher.processQueue)
     * can dequeue ONE job and fan it out to EVERY active channel, instead of each channel
     * competing to dequeue from the same shared queue (which meant only the first channel in
     * the loop ever got a job whenever there were fewer queued jobs than channels).
     */
    public NotificationResult deliver(ScheduledNotification job, NotificationChannel channel) {
        if (job == null) return null;

        try {
            boolean success = channel.send(job.getTemplate());
            if (success) {
                return new NotificationResult(job.getContext().getNotificationId(), true, channel.getChannelName(), null);
            } else {
                retryJob.handleRetry(queue, job);
                return new NotificationResult(job.getContext().getNotificationId(), false, channel.getChannelName(), "Delivery returned false.");
            }
        } catch (Exception e) {
            retryJob.handleRetry(queue, job);
            return new NotificationResult(job.getContext().getNotificationId(), false, channel.getChannelName(), e.getMessage());
        }
    }
}