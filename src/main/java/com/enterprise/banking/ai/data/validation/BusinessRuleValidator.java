package com.enterprise.banking.ai.data.validation;

import com.enterprise.banking.ai.data.model.GeneratedField;

/**
 * Validates generated fields against core enterprise banking logic.
 */
public class BusinessRuleValidator {

    /**
     * Validates if a generated transfer amount conforms to standard logic rules.
     *
     * @param field The field to validate
     * @return true if valid context, false otherwise
     */
    public static boolean validateTransferAmount(GeneratedField field) {
        if (field == null || field.getFieldValue() == null) return false;
        
        // If it's explicitly designed to be a negative path, we bypass strict business limits
        if (field.isNegativePath()) return true;

        try {
            double amount = Double.parseDouble(field.getFieldValue().toString());
            return amount > 0 && amount <= 9999999.99;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates standard enterprise email formats.
     *
     * @param field The field to validate
     * @return true if valid context, false otherwise
     */
    public static boolean validateEmail(GeneratedField field) {
        if (field == null || field.getFieldValue() == null) return false;
        if (field.isNegativePath()) return true;
        
        String email = field.getFieldValue().toString();
        return email.contains("@") && email.contains(".");
    }
}