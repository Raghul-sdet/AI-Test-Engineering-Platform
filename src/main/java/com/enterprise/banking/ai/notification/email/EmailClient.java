package com.enterprise.banking.ai.notification.email;

import java.util.logging.Logger;

/**
 * Simulated SMTP client executing outbound email transfers.
 */
public class EmailClient {
    private static final Logger LOGGER = Logger.getLogger(EmailClient.class.getName());
    
    private final String smtpHost;

    public EmailClient(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public boolean dispatch(EmailMessage message) {
        LOGGER.info(String.format("Sending Email via [%s] to %s: %s", smtpHost, message.getRecipient(), message.getSubject()));
        // Architectural simulated execution
        return true;
    }
}