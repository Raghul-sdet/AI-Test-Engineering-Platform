package com.enterprise.banking.ai.notification.slack;

import com.enterprise.banking.ai.notification.NotificationChannel;
import com.enterprise.banking.ai.notification.NotificationConfiguration;
import com.enterprise.banking.ai.notification.NotificationTemplate;
import java.util.logging.Logger;

/**
 * Notification Channel implementation for Slack integration.
 */
public class SlackConnector implements NotificationChannel {
    private static final Logger LOGGER = Logger.getLogger(SlackConnector.class.getName());
    private SlackWebhookClient client;

    @Override
    public boolean initialize(NotificationConfiguration config) {
        String webhookUrl = config.getProperty("slack.webhook.url", "https://hooks.slack.com/services/mock");
        this.client = new SlackWebhookClient(webhookUrl);
        LOGGER.info("Slack Connector Initialized.");
        return true;
    }

    @Override
    public boolean send(NotificationTemplate template) {
        if (client == null) throw new IllegalStateException("SlackConnector not initialized.");
        SlackMessage message = new SlackMessage(template);
        return client.postMessage(message);
    }

    @Override
    public String getChannelName() {
        return "SLACK";
    }
}