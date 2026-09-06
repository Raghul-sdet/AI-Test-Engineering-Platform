package com.enterprise.banking.ai.notification;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable DTO representing a structured message payload designed for multi-channel broadcasting.
 */
public class NotificationTemplate {
    private final String title;
    private final String message;
    private final NotificationSeverity severity;
    private final Map<String, String> metadata;

    private NotificationTemplate(Builder builder) {
        this.title = builder.title;
        this.message = builder.message;
        this.severity = builder.severity;
        this.metadata = builder.metadata;
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public NotificationSeverity getSeverity() { return severity; }
    public Map<String, String> getMetadata() { return new HashMap<>(metadata); }

    public static class Builder {
        private String title;
        private String message;
        private NotificationSeverity severity = NotificationSeverity.INFO;
        private Map<String, String> metadata = new HashMap<>();

        public Builder title(String title) { this.title = title; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder severity(NotificationSeverity severity) { this.severity = severity; return this; }
        public Builder addMetadata(String key, String value) { this.metadata.put(key, value); return this; }

        public NotificationTemplate build() {
            if (title == null || message == null) throw new IllegalStateException("Title and Message are required.");
            return new NotificationTemplate(this);
        }
    }
}