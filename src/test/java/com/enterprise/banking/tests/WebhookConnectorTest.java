package com.enterprise.banking.tests;

import com.enterprise.banking.ai.notification.NotificationConfiguration;
import com.enterprise.banking.ai.notification.webhook.WebhookConnector;
import com.enterprise.banking.ai.notification.report.PerformanceNotification;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Validates JSON mapping for generic internal webhook dashboards.
 */
public class WebhookConnectorTest {

    @Test
    public void testWebhookInitializationAndSend() {
        WebhookConnector connector = new WebhookConnector();
        connector.initialize(new NotificationConfiguration());
        
        boolean sent = connector.send(PerformanceNotification.create("CPU Utilization exceeding 90%"));
        Assert.assertTrue(sent, "Webhook Connector must serialize standard JSON and mock HTTP 200 return.");
    }
}