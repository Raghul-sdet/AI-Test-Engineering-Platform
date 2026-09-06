package com.enterprise.banking.ai.notification;

/**
 * DTO representing the outcome of a dispatched notification.
 */
public class NotificationResult {
    private final String notificationId;
    private final boolean successful;
    private final String channelName;
    private final String errorDetails;

    public NotificationResult(String notificationId, boolean successful, String channelName, String errorDetails) {
        this.notificationId = notificationId;
        this.successful = successful;
        this.channelName = channelName;
        this.errorDetails = errorDetails;
    }

    public String getNotificationId() { return notificationId; }
    public boolean isSuccessful() { return successful; }
    public String getChannelName() { return channelName; }
    public String getErrorDetails() { return errorDetails; }
}