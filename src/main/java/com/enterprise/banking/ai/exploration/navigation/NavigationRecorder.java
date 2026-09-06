package com.enterprise.banking.ai.exploration.navigation;

import java.util.logging.Logger;

/**
 * Responsible for physical serialization/logging of navigation paths for auditing.
 */
public class NavigationRecorder {
    private static final Logger LOGGER = Logger.getLogger(NavigationRecorder.class.getName());

    public void recordTransition(NavigationNode from, NavigationNode to, String action) {
        String fromLabel = from != null ? from.getPageTitle() : "START";
        String toLabel = to != null ? to.getPageTitle() : "UNKNOWN";
        LOGGER.info(String.format("Exploration Transition: [%s] --(%s)--> [%s]", fromLabel, action, toLabel));
    }
}