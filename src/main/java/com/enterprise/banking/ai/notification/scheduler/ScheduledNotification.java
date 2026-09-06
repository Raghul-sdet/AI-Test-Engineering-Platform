package com.enterprise.banking.ai.notification.scheduler;

import com.enterprise.banking.ai.notification.NotificationContext;
import com.enterprise.banking.ai.notification.NotificationTemplate;

/**
 * Combines context and template data for routing through the asynchronous queue.
 */
public class ScheduledNotification {
    private final NotificationContext context;
    private final NotificationTemplate template;
    private int attemptCount = 0;

    public ScheduledNotification(NotificationContext context, NotificationTemplate template) {
        this.context = context;
        this.template = template;
    }

    public NotificationContext getContext() { return context; }
    public NotificationTemplate getTemplate() { return template; }
    public int getAttemptCount() { return attemptCount; }
    public void incrementAttempt() { this.attemptCount++; }
}