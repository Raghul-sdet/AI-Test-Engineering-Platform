package com.enterprise.banking.ai.notification.teams;

import com.enterprise.banking.ai.notification.NotificationChannel;
import com.enterprise.banking.ai.notification.NotificationConfiguration;
import com.enterprise.banking.ai.notification.NotificationTemplate;
import java.util.logging.Logger;

/**
 * Notification Channel implementation for Microsoft Teams integration.
 */
public class TeamsConnector implements NotificationChannel {
    private static final Logger LOGGER = Logger.getLogger(TeamsConnector.class.getName());
    private TeamsWebhookClient client;
    private final AdaptiveCardBuilder cardBuilder;

    public TeamsConnector() {
        this.cardBuilder = new AdaptiveCardBuilder();
    }

    @Override
    public boolean initialize(NotificationConfiguration config) {
        String webhookUrl = config.getProperty("teams.webhook.url", "https://outlook.office.com/webhook/mock");
        this.client = new TeamsWebhookClient(webhookUrl);
        LOGGER.info("Teams Connector Initialized.");
        return true;
    }

    @Override
    public boolean send(NotificationTemplate template) {
        if (client == null) throw new IllegalStateException("TeamsConnector not initialized.");
        String json = cardBuilder.buildCardJson(template);
        TeamsMessage message = new TeamsMessage(json);
        return client.postMessage(message);
    }

    @Override
    public String getChannelName() {
        return "MS_TEAMS";
    }
}