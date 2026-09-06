package com.enterprise.banking.ai.notification.report;

import com.enterprise.banking.ai.notification.NotificationSeverity;
import com.enterprise.banking.ai.notification.NotificationTemplate;

/**
 * Factory generating alerts for detected security vulnerabilities.
 */
public class SecurityNotification {

    public static NotificationTemplate create(int criticalCount, String summaryNarrative) {
        return new NotificationTemplate.Builder()
                .title("Security Intelligence Alert")
                .message(String.format("AI Engine identified %d Critical vulnerabilities.\n%s", criticalCount, summaryNarrative))
                .severity(NotificationSeverity.CRITICAL)
                .build();
    }
}