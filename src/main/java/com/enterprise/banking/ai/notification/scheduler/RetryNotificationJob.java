package com.enterprise.banking.ai.notification.scheduler;

import java.util.logging.Logger;

/**
 * Handles delayed re-queuing of failed notifications to ensure delivery guarantees.
 */
public class RetryNotificationJob {
    private static final Logger LOGGER = Logger.getLogger(RetryNotificationJob.class.getName());
    private static final int MAX_RETRIES = 3;

    public void handleRetry(NotificationQueue queue, ScheduledNotification notification) {
        notification.incrementAttempt();
        if (notification.getAttemptCount() <= MAX_RETRIES) {
            LOGGER.warning(String.format("Scheduling retry %d for notification: %s", 
                    notification.getAttemptCount(), notification.getContext().getNotificationId()));
            queue.enqueue(notification);
        } else {
            LOGGER.severe("Max retries exceeded for notification: " + notification.getContext().getNotificationId() + ". Message dropped.");
        }
    }
}