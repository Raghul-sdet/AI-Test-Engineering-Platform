package com.enterprise.banking.ai.notification.webhook;

/**
 * Standard HTTP response from a generic webhook endpoint.
 */
public class WebhookResponse {
    private final int statusCode;

    public WebhookResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccessful() { return statusCode >= 200 && statusCode < 300; }
}