package com.enterprise.banking.ai.notification.teams;

import com.enterprise.banking.ai.notification.NotificationTemplate;

/**
 * Translates generic templates into Microsoft Teams Adaptive Card JSON structures.
 */
public class AdaptiveCardBuilder {

    public String buildCardJson(NotificationTemplate template) {
        // Simulated JSON payload builder for Architectural representation
        return String.format(
            "{ \"type\": \"message\", \"attachments\": [ { \"contentType\": \"application/vnd.microsoft.card.adaptive\", \"content\": { \"type\": \"AdaptiveCard\", \"body\": [ { \"type\": \"TextBlock\", \"text\": \"%s\", \"weight\": \"bolder\" }, { \"type\": \"TextBlock\", \"text\": \"%s\" } ] } } ] }",
            template.getTitle().replace("\"", "\\\""),
            template.getMessage().replace("\"", "\\\"")
        );
    }
}