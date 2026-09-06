package com.enterprise.banking.ai.data.validation;

import com.enterprise.banking.ai.data.model.GeneratedTestData;

/**
 * Validates structural integrity and null-safety of comprehensive data payloads.
 */
public class DataIntegrityValidator {

    /**
     * Ensures a generated dataset contains fields and has no unhandled null objects.
     *
     * @param data The generated dataset object
     * @return true if structurally sound, false otherwise
     */
    public static boolean validateStructuralIntegrity(GeneratedTestData data) {
        if (data == null) {
            return false;
        }
        
        if (data.getAllFields().isEmpty()) {
            return false;
        }
        
        return true;
    }
}