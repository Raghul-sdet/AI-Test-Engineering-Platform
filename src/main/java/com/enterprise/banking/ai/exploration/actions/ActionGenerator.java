package com.enterprise.banking.ai.exploration.actions;

/**
 * Formulates synthetic test data (payloads) to satisfy the ActionPlanner's targeted UI interaction.
 */
public class ActionGenerator {

    public String generatePayloadForAction(String actionType) {
        if ("SUBMIT_FORM".equals(actionType)) {
            return "{ \"username\": \"exploratory_user\", \"password\": \"AutoGen123!\" }";
        }
        return "";
    }
}