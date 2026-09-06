package com.enterprise.banking.ai.notification.teams;

import java.util.logging.Logger;

/**
 * HTTP Client executing outbound webhook payloads to Microsoft Teams.
 */
public class TeamsWebhookClient {
    private static final Logger LOGGER = Logger.getLogger(TeamsWebhookClient.class.getName());
    private final String webhookUrl;

    public TeamsWebhookClient(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public boolean postMessage(TeamsMessage message) {
        LOGGER.info(String.format("Posting to MS Teams Webhook [%s]. Payload size: %d bytes", webhookUrl, message.getPayloadJson().length()));
        // Architectural simulated HTTP POST
        return true;
    }
}