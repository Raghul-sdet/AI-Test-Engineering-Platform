package com.enterprise.banking.tests;

import com.enterprise.banking.ai.notification.NotificationConfiguration;
import com.enterprise.banking.ai.notification.email.EmailConnector;
import com.enterprise.banking.ai.notification.report.DashboardNotification;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Validates standard SMTP email template generation and execution.
 */
public class EmailConnectorTest {

    @Test
    public void testEmailInitializationAndSend() {
        EmailConnector connector = new EmailConnector();
        connector.initialize(new NotificationConfiguration());
        
        boolean sent = connector.send(DashboardNotification.create("http://dashboard.local"));
        Assert.assertTrue(sent, "Email Connector must format HTML template and execute SMTP mock dispatch.");
    }
}