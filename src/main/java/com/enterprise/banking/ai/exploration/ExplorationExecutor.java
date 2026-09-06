package com.enterprise.banking.ai.exploration;

import com.enterprise.banking.ai.exploration.actions.ActionExecutor;
import com.enterprise.banking.ai.exploration.actions.ActionGenerator;
import com.enterprise.banking.ai.exploration.actions.ActionHistory;

/**
 * Drives the state progression loop, triggering physical actions and payload generation.
 */
public class ExplorationExecutor {
    private final ActionExecutor actionExecutor;
    private final ActionGenerator actionGenerator;

    public ExplorationExecutor(ActionHistory history) {
        this.actionExecutor = new ActionExecutor(history);
        this.actionGenerator = new ActionGenerator();
    }

    public boolean executeStep(String targetAction) {
        if (targetAction == null) return false;
        String payload = actionGenerator.generatePayloadForAction(targetAction);
        return actionExecutor.execute(targetAction, payload);
    }
}