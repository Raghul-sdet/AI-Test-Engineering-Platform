package com.enterprise.banking.ai.notification.slack;

/**
 * DTO representing a Slack BlockKit Attachment.
 */
public class SlackAttachment {
    private final String color;
    private final String text;

    public SlackAttachment(String color, String text) {
        this.color = color;
        this.text = text;
    }

    public String getColor() { return color; }
    public String getText() { return text; }
}