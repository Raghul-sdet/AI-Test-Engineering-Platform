package com.enterprise.banking.tests;

import com.enterprise.banking.ai.notification.NotificationConfiguration;
import com.enterprise.banking.ai.notification.slack.SlackConnector;
import com.enterprise.banking.ai.notification.report.FailureNotification;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Validates initialization and dispatch behavior of the Slack Webhook integration.
 */
public class SlackConnectorTest {

    @Test
    public void testSlackInitializationAndSend() {
        SlackConnector connector = new SlackConnector();
        boolean init = connector.initialize(new NotificationConfiguration());
        
        Assert.assertTrue(init, "Slack Connector must successfully initialize via config.");
        
        boolean sent = connector.send(FailureNotification.create("LoginTest", "NullPointerException at line 42"));
        Assert.assertTrue(sent, "Connector must successfully post message via mock client.");
    }
}