package com.enterprise.banking.tests;

import com.enterprise.banking.ai.notification.NotificationConfiguration;
import com.enterprise.banking.ai.notification.teams.TeamsConnector;
import com.enterprise.banking.ai.notification.report.SecurityNotification;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Validates initialization and Adaptive Card formatting behavior of MS Teams integration.
 */
public class TeamsConnectorTest {

    @Test
    public void testTeamsInitializationAndSend() {
        TeamsConnector connector = new TeamsConnector();
        connector.initialize(new NotificationConfiguration());
        
        boolean sent = connector.send(SecurityNotification.create(3, "CRITICAL: SQLi Found"));
        Assert.assertTrue(sent, "Teams Connector must serialize AdaptiveCard and post message successfully.");
    }
}