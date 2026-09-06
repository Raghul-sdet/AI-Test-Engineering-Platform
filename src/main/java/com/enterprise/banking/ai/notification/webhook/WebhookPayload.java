package com.enterprise.banking.ai.notification.webhook;

import com.enterprise.banking.ai.notification.NotificationTemplate;
import java.util.HashMap;
import java.util.Map;

/**
 * Constructs a generic JSON-serializable map from a NotificationTemplate.
 */
public class WebhookPayload {
    private final Map<String, Object> payload;

    public WebhookPayload(NotificationTemplate template) {
        this.payload = new HashMap<>();
        payload.put("title", template.getTitle());
        payload.put("message", template.getMessage());
        payload.put("severity", template.getSeverity().name());
        payload.put("metadata", template.getMetadata());
    }

    public Map<String, Object> getPayloadMap() { return new HashMap<>(payload); }
}