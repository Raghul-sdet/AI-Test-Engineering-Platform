package com.enterprise.banking.ai.data.strategy;

import com.enterprise.banking.ai.data.generator.EdgeCaseGenerator;
import com.enterprise.banking.ai.data.generator.RandomDataEngine;
import com.enterprise.banking.ai.data.model.DataGenerationRequest;
import com.enterprise.banking.ai.data.model.GeneratedField;
import com.enterprise.banking.ai.data.model.GeneratedTestData;

/**
 * Strategy responsible for synthesizing customer demographic and profile data.
 */
public class CustomerStrategy implements GenerationStrategy {

    @Override
    public boolean supports(String targetEntity) {
        String target = targetEntity.toLowerCase();
        return target.contains("customer") || target.contains("profile") || target.contains("account");
    }

    @Override
    public GeneratedTestData synthesizeData(DataGenerationRequest request) {
        GeneratedTestData data = new GeneratedTestData();
        data.setRelatedContext("CustomerProfile");

        if (request.isRequiresEdgeCases()) {
            data.addField(new GeneratedField("firstName", EdgeCaseGenerator.generateUnicodeString(), "String", false));
            data.addField(new GeneratedField("lastName", EdgeCaseGenerator.generateEmojiString(), "String", false));
        } else {
            data.addField(new GeneratedField("firstName", "John" + RandomDataEngine.generateRandomString(4), "String", false));
            data.addField(new GeneratedField("lastName", "Doe" + RandomDataEngine.generateRandomString(4), "String", false));
        }
        
        data.addField(new GeneratedField("email", RandomDataEngine.generateUniqueEmail(), "String", false));
        data.addField(new GeneratedField("phoneNumber", "9" + RandomDataEngine.generateRandomNumericString(9), "String", false));

        return data;
    }
}