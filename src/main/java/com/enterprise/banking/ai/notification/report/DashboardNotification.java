package com.enterprise.banking.ai.notification.report;

import com.enterprise.banking.ai.notification.NotificationSeverity;
import com.enterprise.banking.ai.notification.NotificationTemplate;

/**
 * Factory generating routing instructions for aggregate execution dashboards.
 */
public class DashboardNotification {

    public static NotificationTemplate create(String dashboardUrl) {
        return new NotificationTemplate.Builder()
                .title("Executive Dashboard Published")
                .message("The latest AI Automation Executive Dashboard is available: " + dashboardUrl)
                .severity(NotificationSeverity.INFO)
                .build();
    }
}