package com.enterprise.banking.ai.exploration.actions;

/**
 * Validates whether an action is inherently safe to execute during exploration 
 * (e.g., avoiding logout links unless specified).
 */
public class ActionValidator {

    public boolean isSafeToExecute(String actionDescription) {
        if (actionDescription == null) return false;
        
        String lower = actionDescription.toLowerCase();
        if (lower.contains("delete") || lower.contains("remove") || lower.contains("logout")) {
            return false; // Risky exploratory actions blocked by default
        }
        return true;
    }
}