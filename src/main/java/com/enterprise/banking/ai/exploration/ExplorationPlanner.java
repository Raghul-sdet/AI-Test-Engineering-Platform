package com.enterprise.banking.ai.exploration;

import com.enterprise.banking.ai.exploration.actions.ActionPlanner;
import com.enterprise.banking.ai.exploration.navigation.NavigationPlanner;

/**
 * Combines structural Navigation constraints with UI Action targeting.
 */
public class ExplorationPlanner {
    private final NavigationPlanner navigationPlanner;
    private final ActionPlanner actionPlanner;

    public ExplorationPlanner(NavigationPlanner nav, ActionPlanner act) {
        this.navigationPlanner = nav;
        this.actionPlanner = act;
    }

    public NavigationPlanner getNavigationPlanner() { return navigationPlanner; }
    public ActionPlanner getActionPlanner() { return actionPlanner; }
}