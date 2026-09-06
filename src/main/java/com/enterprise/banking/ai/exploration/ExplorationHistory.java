package com.enterprise.banking.ai.exploration;

import com.enterprise.banking.ai.exploration.actions.ActionHistory;
import com.enterprise.banking.ai.exploration.navigation.NavigationHistory;

/**
 * Aggregates all historical logs (Navigation + Actions) into a single audit boundary.
 */
public class ExplorationHistory {
    private final NavigationHistory navigationHistory;
    private final ActionHistory actionHistory;

    public ExplorationHistory(NavigationHistory nav, ActionHistory act) {
        this.navigationHistory = nav;
        this.actionHistory = act;
    }

    public NavigationHistory getNavigationHistory() { return navigationHistory; }
    public ActionHistory getActionHistory() { return actionHistory; }
}