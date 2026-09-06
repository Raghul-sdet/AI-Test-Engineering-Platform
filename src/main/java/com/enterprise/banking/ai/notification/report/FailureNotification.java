package com.enterprise.banking.ai.notification.report;

import com.enterprise.banking.ai.notification.NotificationSeverity;
import com.enterprise.banking.ai.notification.NotificationTemplate;

/**
 * Factory generating a critical alert containing AI RCA details.
 */
public class FailureNotification {

    public static NotificationTemplate create(String testName, String aiRcaSummary) {
        return new NotificationTemplate.Builder()
                .title("CRITICAL: Test Failure Detected")
                .message(String.format("Test [%s] failed.\nAI RCA Analysis: %s", testName, aiRcaSummary))
                .severity(NotificationSeverity.ERROR)
                .build();
    }
}