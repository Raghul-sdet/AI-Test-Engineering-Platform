package com.enterprise.banking.ai.notification.email;

import com.enterprise.banking.ai.notification.NotificationChannel;
import com.enterprise.banking.ai.notification.NotificationConfiguration;
import com.enterprise.banking.ai.notification.NotificationTemplate;
import java.util.logging.Logger;

/**
 * Notification Channel implementation for Enterprise SMTP integration.
 */
public class EmailConnector implements NotificationChannel {
    private static final Logger LOGGER = Logger.getLogger(EmailConnector.class.getName());
    private EmailClient client;
    private String defaultRecipient;

    @Override
    public boolean initialize(NotificationConfiguration config) {
        String host = config.getProperty("smtp.host", "smtp.enterprise.local");
        this.defaultRecipient = config.getProperty("email.recipient", "qa-alerts@enterprise.local");
        this.client = new EmailClient(host);
        LOGGER.info("Email Connector Initialized.");
        return true;
    }

    @Override
    public boolean send(NotificationTemplate template) {
        if (client == null) throw new IllegalStateException("EmailConnector not initialized.");
        EmailMessage message = EmailTemplate.parse(template, defaultRecipient);
        return client.dispatch(message);
    }

    @Override
    public String getChannelName() {
        return "EMAIL";
    }
}