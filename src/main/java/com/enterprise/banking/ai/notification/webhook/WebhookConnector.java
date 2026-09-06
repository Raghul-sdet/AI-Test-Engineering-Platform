package com.enterprise.banking.ai.notification.webhook;

import com.enterprise.banking.ai.notification.NotificationChannel;
import com.enterprise.banking.ai.notification.NotificationConfiguration;
import com.enterprise.banking.ai.notification.NotificationTemplate;
import java.util.logging.Logger;

/**
 * Generic Notification Channel implementation for routing to bespoke internal dashboards or APIs.
 */
public class WebhookConnector implements NotificationChannel {
    private static final Logger LOGGER = Logger.getLogger(WebhookConnector.class.getName());
    private WebhookClient client;

    @Override
    public boolean initialize(NotificationConfiguration config) {
        String url = config.getProperty("generic.webhook.url", "http://internal-dashboard.local/api/notify");
        this.client = new WebhookClient(url);
        LOGGER.info("Generic Webhook Connector Initialized.");
        return true;
    }

    @Override
    public boolean send(NotificationTemplate template) {
        if (client == null) throw new IllegalStateException("WebhookConnector not initialized.");
        WebhookPayload payload = new WebhookPayload(template);
        return client.sendPayload(payload).isSuccessful();
    }

    @Override
    public String getChannelName() {
        return "GENERIC_WEBHOOK";
    }
}