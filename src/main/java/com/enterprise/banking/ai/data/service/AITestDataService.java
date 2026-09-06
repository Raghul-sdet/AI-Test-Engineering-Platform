package com.enterprise.banking.ai.data.service;

import com.enterprise.banking.ai.data.model.DataGenerationRequest;
import com.enterprise.banking.ai.data.model.DataGenerationResponse;
import com.enterprise.banking.ai.data.model.GeneratedDataset;
import com.enterprise.banking.ai.data.model.GeneratedTestData;
import com.enterprise.banking.ai.data.strategy.AuthenticationStrategy;
import com.enterprise.banking.ai.data.strategy.CustomerStrategy;
import com.enterprise.banking.ai.data.strategy.GenerationStrategy;
import com.enterprise.banking.ai.data.strategy.TransferStrategy;
import com.enterprise.banking.ai.data.validation.GeneratedDataValidator;
import com.enterprise.banking.ai.exception.TestDataGenerationException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Facade orchestrating the intelligent generation, validation, and storage 
 * of synthesized data targeting AI mapped Execution Scenarios.
 */
public class AITestDataService {

    private static final Logger LOGGER = Logger.getLogger(AITestDataService.class.getName());

    private final List<GenerationStrategy> strategies;
    private final GeneratedDataValidator validator;
    private final GeneratedDataRepository repository;

    /**
     * Initializes the service, its strategies, and repository.
     */
    public AITestDataService() {
        this.strategies = new ArrayList<>();
        this.strategies.add(new AuthenticationStrategy());
        this.strategies.add(new TransferStrategy());
        this.strategies.add(new CustomerStrategy());

        this.validator = new GeneratedDataValidator();
        this.repository = new GeneratedDataRepository();
    }

    /**
     * Coordinates the fulfillment of a data generation request.
     *
     * @param request The data criteria requested
     * @return DataGenerationResponse wrapping status and the generated payload
     * @throws TestDataGenerationException if logic completely fails
     */
    public DataGenerationResponse generateTestData(DataGenerationRequest request) {
        if (request == null || request.getTargetEntity() == null || request.getScenarioId() == null) {
            throw new TestDataGenerationException("Invalid generation request parameters.");
        }

        DataGenerationResponse response = new DataGenerationResponse();
        response.setRequestId("REQ-" + System.currentTimeMillis());

        try {
            LOGGER.info("Initiating test data synthesis for Scenario: " + request.getScenarioId());
            GenerationStrategy activeStrategy = selectStrategy(request.getTargetEntity());

            GeneratedDataset dataset = new GeneratedDataset();
            dataset.setScenarioId(request.getScenarioId());

            for (int i = 0; i < request.getRequestedRecordCount(); i++) {
                GeneratedTestData record = activeStrategy.synthesizeData(request);
                
                if (validator.validateGeneratedRecord(record)) {
                    dataset.addRecord(record);
                } else {
                    LOGGER.warning("Data record failed validation and was excluded from the final dataset.");
                }
            }

            if (dataset.getSize() > 0) {
                repository.save(dataset);
                response.setDataset(dataset);
                response.setSuccessful(true);
                response.setStatusMessage("Generated " + dataset.getSize() + " robust data records successfully.");
            } else {
                response.setSuccessful(false);
                response.setStatusMessage("Synthesis completed but zero valid records were produced.");
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Fatal failure during data synthesis.", ex);
            throw new TestDataGenerationException("AI Synthesizer engine failed to fulfill request.", ex);
        }

        return response;
    }

    private GenerationStrategy selectStrategy(String targetEntity) {
        for (GenerationStrategy strategy : strategies) {
            if (strategy.supports(targetEntity)) {
                LOGGER.fine("Selected strategy: " + strategy.getClass().getSimpleName());
                return strategy;
            }
        }
        LOGGER.warning("No specific strategy found for target entity. Defaulting to CustomerStrategy.");
        return new CustomerStrategy(); // Fallback strategy
    }
    
    /**
     * Exposes repository for fetching generated payloads during TestNG execution.
     *
     * @return GeneratedDataRepository
     */
    public GeneratedDataRepository getRepository() {
        return this.repository;
    }
}