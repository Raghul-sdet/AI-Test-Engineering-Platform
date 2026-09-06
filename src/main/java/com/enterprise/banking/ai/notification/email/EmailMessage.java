package com.enterprise.banking.ai.notification.email;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO representing an outgoing SMTP email message.
 */
public class EmailMessage {
    private final String subject;
    private final String body;
    private final String recipient;
    private final List<EmailAttachment> attachments;

    public EmailMessage(String subject, String body, String recipient) {
        this.subject = subject;
        this.body = body;
        this.recipient = recipient;
        this.attachments = new ArrayList<>();
    }

    public void addAttachment(EmailAttachment attachment) {
        if (attachment != null) attachments.add(attachment);
    }

    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public String getRecipient() { return recipient; }
    public List<EmailAttachment> getAttachments() { return new ArrayList<>(attachments); }
}