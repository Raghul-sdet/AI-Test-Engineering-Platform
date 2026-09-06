package com.enterprise.banking.ai.data.strategy;

import com.enterprise.banking.ai.data.generator.BoundaryValueGenerator;
import com.enterprise.banking.ai.data.generator.NegativeDataGenerator;
import com.enterprise.banking.ai.data.generator.RandomDataEngine;
import com.enterprise.banking.ai.data.model.DataGenerationRequest;
import com.enterprise.banking.ai.data.model.GeneratedField;
import com.enterprise.banking.ai.data.model.GeneratedTestData;

/**
 * Strategy responsible for synthesizing financial transaction and beneficiary data.
 */
public class TransferStrategy implements GenerationStrategy {

    @Override
    public boolean supports(String targetEntity) {
        String target = targetEntity.toLowerCase();
        return target.contains("transfer") || target.contains("payment") || target.contains("transaction");
    }

    @Override
    public GeneratedTestData synthesizeData(DataGenerationRequest request) {
        GeneratedTestData data = new GeneratedTestData();
        data.setRelatedContext("PaymentTransfer");

        data.addField(new GeneratedField("beneficiaryAccount", RandomDataEngine.generateRandomNumericString(12), "String", false));
        data.addField(new GeneratedField("ifscCode", "HDFC000" + RandomDataEngine.generateRandomNumericString(4), "String", false));

        if (request.isRequiresNegativeData()) {
            data.addField(new GeneratedField("transferAmount", NegativeDataGenerator.generateNegativeAmount(), "Double", true));
        } else if (request.isRequiresBoundaryData()) {
            data.addField(new GeneratedField("transferAmount", BoundaryValueGenerator.generateMaximumTransactionAmount(), "Double", false));
        } else {
            data.addField(new GeneratedField("transferAmount", RandomDataEngine.generateRandomDouble(100.0, 5000.0), "Double", false));
        }

        return data;
    }
}