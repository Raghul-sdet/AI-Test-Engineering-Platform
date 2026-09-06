package com.enterprise.banking.ai.data.strategy;

import com.enterprise.banking.ai.data.model.DataGenerationRequest;
import com.enterprise.banking.ai.data.model.GeneratedTestData;

/**
 * Interface defining the strict contract for specific business data synthesizers.
 */
public interface GenerationStrategy {

    /**
     * Determines if this strategy implementation supports the target entity.
     *
     * @param targetEntity The target entity requested (e.g., "customer", "transfer")
     * @return true if supported, false otherwise
     */
    boolean supports(String targetEntity);

    /**
     * Orchestrates the synthesis of a data record based on specific logic.
     *
     * @param request The generation request parameters
     * @return A populated test data record
     */
    GeneratedTestData synthesizeData(DataGenerationRequest request);
}