package com.enterprise.banking.ai.notification;

import java.util.UUID;

/**
 * Encapsulates the runtime context for an individual notification request.
 */
public class NotificationContext {
    private final String notificationId;
    private final String sourceEvent;
    private final NotificationPriority priority;

    public NotificationContext(String sourceEvent, NotificationPriority priority) {
        this.notificationId = "NOTIF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.sourceEvent = sourceEvent;
        this.priority = priority;
    }

    public String getNotificationId() { return notificationId; }
    public String getSourceEvent() { return sourceEvent; }
    public NotificationPriority getPriority() { return priority; }
}