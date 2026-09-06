package com.enterprise.banking.ai.data.strategy;

import com.enterprise.banking.ai.data.generator.BoundaryValueGenerator;
import com.enterprise.banking.ai.data.generator.NegativeDataGenerator;
import com.enterprise.banking.ai.data.generator.RandomDataEngine;
import com.enterprise.banking.ai.data.model.DataGenerationRequest;
import com.enterprise.banking.ai.data.model.GeneratedField;
import com.enterprise.banking.ai.data.model.GeneratedTestData;

/**
 * Strategy responsible for synthesizing login, registration, and security credentials.
 */
public class AuthenticationStrategy implements GenerationStrategy {

    @Override
    public boolean supports(String targetEntity) {
        String target = targetEntity.toLowerCase();
        return target.contains("auth") || target.contains("login") || target.contains("security");
    }

    @Override
    public GeneratedTestData synthesizeData(DataGenerationRequest request) {
        GeneratedTestData data = new GeneratedTestData();
        data.setRelatedContext("Authentication");

        if (request.isRequiresNegativeData()) {
            data.addField(new GeneratedField("username", NegativeDataGenerator.generateSqlInjectionString(), "String", true));
            data.addField(new GeneratedField("password", BoundaryValueGenerator.generateMinimumLengthString(), "String", true));
        } else {
            data.addField(new GeneratedField("username", "user_" + RandomDataEngine.generateRandomNumericString(6), "String", false));
            data.addField(new GeneratedField("password", "P@ssw0rd123_" + RandomDataEngine.generateRandomString(4), "String", false));
        }

        return data;
    }
}