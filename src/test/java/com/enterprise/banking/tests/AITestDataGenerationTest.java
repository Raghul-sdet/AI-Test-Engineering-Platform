package com.enterprise.banking.tests;

import com.enterprise.banking.ai.data.model.DataGenerationRequest;
import com.enterprise.banking.ai.data.model.DataGenerationResponse;
import com.enterprise.banking.ai.data.model.GeneratedTestData;
import com.enterprise.banking.ai.data.service.AITestDataService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Validates the core AI Test Data Synthesizer logic, boundary generation, 
 * negative data construction, and repository persistence.
 */
public class AITestDataGenerationTest {

    private AITestDataService dataService;

    @BeforeMethod
    public void setUp() {
        dataService = new AITestDataService();
        dataService.getRepository().clear();
    }

    @Test
    public void testPositiveTransferDataGeneration() {
        DataGenerationRequest request = new DataGenerationRequest();
        request.setScenarioId("SCEN-POS-01");
        request.setTargetEntity("transfer");
        request.setRequestedRecordCount(1);
        request.setRequiresNegativeData(false);

        DataGenerationResponse response = dataService.generateTestData(request);

        Assert.assertTrue(response.isSuccessful(), "Data generation should be successful.");
        Assert.assertNotNull(response.getDataset(), "Dataset should not be null.");
        Assert.assertEquals(response.getDataset().getSize(), 1, "Dataset should contain exactly 1 record.");

        GeneratedTestData record = response.getDataset().getRecords().get(0);
        Assert.assertNotNull(record.getField("transferAmount"), "Transfer amount field should exist.");
        Assert.assertFalse(record.containsNegativeData(), "Positive request should not contain negative paths.");
        
        // Validate persistence
        Assert.assertNotNull(dataService.getRepository().retrieve("SCEN-POS-01"), "Data must be retrievable from repository.");
    }

    @Test
    public void testNegativeAuthenticationDataGeneration() {
        DataGenerationRequest request = new DataGenerationRequest();
        request.setScenarioId("SCEN-NEG-02");
        request.setTargetEntity("authentication");
        request.setRequestedRecordCount(2);
        request.setRequiresNegativeData(true);

        DataGenerationResponse response = dataService.generateTestData(request);

        Assert.assertTrue(response.isSuccessful(), "Data generation should be successful.");
        Assert.assertEquals(response.getDataset().getSize(), 2, "Should generate exactly 2 negative records.");

        GeneratedTestData record = response.getDataset().getRecords().get(0);
        Assert.assertTrue(record.containsNegativeData(), "Record should contain explicit negative paths.");
        
        String username = record.getField("username").getValueAsString();
        Assert.assertTrue(username.contains("OR '1'='1'"), "Username should contain SQLi payload.");
    }

    @Test
    public void testEdgeCaseCustomerDataGeneration() {
        DataGenerationRequest request = new DataGenerationRequest();
        request.setScenarioId("SCEN-EDGE-03");
        request.setTargetEntity("customer");
        request.setRequestedRecordCount(1);
        request.setRequiresEdgeCases(true);

        DataGenerationResponse response = dataService.generateTestData(request);

        Assert.assertTrue(response.isSuccessful(), "Data generation should be successful.");
        GeneratedTestData record = response.getDataset().getRecords().get(0);
        
        String firstName = record.getField("firstName").getValueAsString();
        String lastName = record.getField("lastName").getValueAsString();
        
        Assert.assertTrue(firstName.length() > 0, "First name should be populated with Edge Data.");
        Assert.assertTrue(lastName.length() > 0, "Last name should be populated with Edge Data.");
    }
}