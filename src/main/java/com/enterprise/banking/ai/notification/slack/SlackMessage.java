package com.enterprise.banking.ai.notification.slack;

import com.enterprise.banking.ai.notification.NotificationTemplate;
import java.util.ArrayList;
import java.util.List;

/**
 * Formats a generic notification template into a Slack-specific webhook payload.
 */
public class SlackMessage {
    private final String text;
    private final List<SlackAttachment> attachments;

    public SlackMessage(NotificationTemplate template) {
        this.text = "*" + template.getTitle() + "*";
        this.attachments = new ArrayList<>();
        
        String color = switch (template.getSeverity()) {
            case CRITICAL, ERROR -> "#FF0000";
            case WARNING -> "#FFA500";
            case SUCCESS -> "#00FF00";
            default -> "#808080";
        };
        
        this.attachments.add(new SlackAttachment(color, template.getMessage()));
    }

    public String getText() { return text; }
    public List<SlackAttachment> getAttachments() { return new ArrayList<>(attachments); }
}