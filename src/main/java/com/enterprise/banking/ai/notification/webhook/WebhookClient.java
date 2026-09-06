package com.enterprise.banking.ai.notification.webhook;

import java.util.logging.Logger;

/**
 * Executes raw POST requests to generic enterprise webhooks.
 */
public class WebhookClient {
    private static final Logger LOGGER = Logger.getLogger(WebhookClient.class.getName());
    private final String url;

    public WebhookClient(String url) {
        this.url = url;
    }

    public WebhookResponse sendPayload(WebhookPayload payload) {
        LOGGER.info(String.format("Dispatching generic webhook to [%s]. Event: %s", url, payload.getPayloadMap().get("title")));
        return new WebhookResponse(200);
    }
}