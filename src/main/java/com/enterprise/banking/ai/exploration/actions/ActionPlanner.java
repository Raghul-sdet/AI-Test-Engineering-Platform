package com.enterprise.banking.ai.exploration.actions;

import java.util.List;
import java.util.logging.Logger;

/**
 * Evaluates available interactable elements and prioritizes which action the 
 * engine should take next based on exploration strategy.
 */
public class ActionPlanner {
    private static final Logger LOGGER = Logger.getLogger(ActionPlanner.class.getName());

    public String planNextAction(List<String> discoveredElements) {
        if (discoveredElements == null || discoveredElements.isEmpty()) {
            LOGGER.warning("ActionPlanner: No interactable elements discovered.");
            return null;
        }
        
        // Prioritize Forms over Buttons over Links for deeper mutation testing
        if (discoveredElements.contains("FORM_BLOCK")) return "SUBMIT_FORM";
        if (discoveredElements.contains("BUTTON_ELEMENTS")) return "CLICK_BUTTON";
        if (discoveredElements.contains("HYPERLINKS")) return "CLICK_LINK";
        
        return "UNKNOWN_ACTION";
    }
}