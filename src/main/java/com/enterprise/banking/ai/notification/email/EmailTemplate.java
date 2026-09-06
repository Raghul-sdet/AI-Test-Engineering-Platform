package com.enterprise.banking.ai.notification.email;

import com.enterprise.banking.ai.notification.NotificationTemplate;

/**
 * Converter translating a generic NotificationTemplate into an HTML-formatted EmailMessage.
 */
public class EmailTemplate {
    
    public static EmailMessage parse(NotificationTemplate template, String targetEmail) {
        String htmlBody = "<h2>" + template.getTitle() + "</h2>" +
                          "<p><b>Severity:</b> " + template.getSeverity() + "</p>" +
                          "<p>" + template.getMessage() + "</p>";
                          
        return new EmailMessage(template.getTitle(), htmlBody, targetEmail);
    }
}