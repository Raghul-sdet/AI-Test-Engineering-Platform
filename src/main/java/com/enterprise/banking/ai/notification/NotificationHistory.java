package com.enterprise.banking.ai.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Thread-safe audit log retaining the history of all processed notifications.
 */
public class NotificationHistory {
    private final List<NotificationResult> historyLogs = new CopyOnWriteArrayList<>();

    public void logResult(NotificationResult result) {
        if (result != null) historyLogs.add(result);
    }

    public List<NotificationResult> getHistory() {
        return new ArrayList<>(historyLogs);
    }
}