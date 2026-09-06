package com.enterprise.banking.ai.notification;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores endpoint URLs, webhooks, and SMTP settings for configured channels.
 */
public class NotificationConfiguration {
    private final Map<String, String> properties = new HashMap<>();

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }
}