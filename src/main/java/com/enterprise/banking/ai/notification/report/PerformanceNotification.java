package com.enterprise.banking.ai.notification.report;

import com.enterprise.banking.ai.notification.NotificationSeverity;
import com.enterprise.banking.ai.notification.NotificationTemplate;

/**
 * Factory generating alerts for detected performance regressions.
 */
public class PerformanceNotification {

    public static NotificationTemplate create(String bottleneckAlert) {
        return new NotificationTemplate.Builder()
                .title("Performance Regression Detected")
                .message("The AI Performance Engine has flagged an anomaly: " + bottleneckAlert)
                .severity(NotificationSeverity.WARNING)
                .build();
    }
}