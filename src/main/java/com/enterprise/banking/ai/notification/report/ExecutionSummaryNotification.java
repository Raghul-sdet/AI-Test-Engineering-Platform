package com.enterprise.banking.ai.notification.report;

import com.enterprise.banking.ai.notification.NotificationSeverity;
import com.enterprise.banking.ai.notification.NotificationTemplate;

/**
 * Factory generating a standard "Suite Complete" notification payload.
 */
public class ExecutionSummaryNotification {
    
    public static NotificationTemplate create(String suiteName, int passed, int failed) {
        NotificationSeverity severity = failed > 0 ? NotificationSeverity.WARNING : NotificationSeverity.SUCCESS;
        
        return new NotificationTemplate.Builder()
                .title("Test Execution Complete: " + suiteName)
                .message(String.format("Execution concluded. Passed: %d | Failed: %d", passed, failed))
                .severity(severity)
                .addMetadata("suite", suiteName)
                .build();
    }
}