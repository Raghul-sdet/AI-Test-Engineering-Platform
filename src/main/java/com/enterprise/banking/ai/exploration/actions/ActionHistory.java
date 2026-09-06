package com.enterprise.banking.ai.exploration.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * Logs a chronological sequence of all actions taken by the engine.
 */
public class ActionHistory {
    private final List<String> executedActions = new ArrayList<>();

    public void logAction(String action) {
        if (action != null) executedActions.add(action);
    }

    public List<String> getExecutedActions() {
        return new ArrayList<>(executedActions);
    }
}