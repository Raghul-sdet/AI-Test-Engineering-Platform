package com.enterprise.banking.ai.data.validation;

import com.enterprise.banking.ai.data.model.GeneratedField;
import com.enterprise.banking.ai.data.model.GeneratedTestData;
import java.util.logging.Logger;

/**
 * Main coordinator for all AI data validations before injection into TestNG frameworks.
 */
public class GeneratedDataValidator {

    private static final Logger LOGGER = Logger.getLogger(GeneratedDataValidator.class.getName());

    /**
     * Performs a holistic check on a data record utilizing sub-validators.
     *
     * @param record The synthesized record
     * @return true if valid and safe to execute, false otherwise
     */
    public boolean validateGeneratedRecord(GeneratedTestData record) {
        LOGGER.info("Executing comprehensive data validation on record: " + record.getDataRecordId());

        if (!DataIntegrityValidator.validateStructuralIntegrity(record)) {
            LOGGER.severe("Record structural integrity check failed.");
            return false;
        }

        for (GeneratedField field : record.getAllFields()) {
            if ("email".equalsIgnoreCase(field.getFieldName()) && !BusinessRuleValidator.validateEmail(field)) {
                LOGGER.warning("Business rule violation: Email format invalid (and not marked negative).");
                return false;
            }
            if (field.getFieldName().toLowerCase().contains("amount") && !BusinessRuleValidator.validateTransferAmount(field)) {
                LOGGER.warning("Business rule violation: Transfer amount invalid (and not marked negative).");
                return false;
            }
        }

        LOGGER.info("Record validated successfully.");
        return true;
    }
}