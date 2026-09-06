package com.enterprise.banking.ai.data.service;

import com.enterprise.banking.ai.data.model.GeneratedDataset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Thread-safe, in-memory repository to persist generated test data payloads,
 * enabling correlation between Execution Plans and data during runtime via scenario IDs.
 */
public class GeneratedDataRepository {

    private static final Logger LOGGER = Logger.getLogger(GeneratedDataRepository.class.getName());
    
    // Key = ScenarioId, Value = Synthesized Dataset
    private final Map<String, GeneratedDataset> repositoryStorage;

    /**
     * Initializes the concurrent repository hash map.
     */
    public GeneratedDataRepository() {
        this.repositoryStorage = new ConcurrentHashMap<>();
    }

    /**
     * Saves a dataset into the repository linked to its driving scenario.
     *
     * @param dataset The dataset to save
     */
    public void save(GeneratedDataset dataset) {
        if (dataset != null && dataset.getScenarioId() != null) {
            repositoryStorage.put(dataset.getScenarioId(), dataset);
            LOGGER.info("Saved generated dataset to repository for Scenario: " + dataset.getScenarioId());
        } else {
            LOGGER.warning("Attempted to save a null dataset or dataset without scenario ID.");
        }
    }

    /**
     * Retrieves a dataset associated with a specific scenario ID.
     *
     * @param scenarioId The scenario ID
     * @return GeneratedDataset, or null if not found
     */
    public GeneratedDataset retrieve(String scenarioId) {
        return repositoryStorage.get(scenarioId);
    }

    /**
     * Clears the active repository.
     */
    public void clear() {
        repositoryStorage.clear();
        LOGGER.info("Generated Data Repository cleared.");
    }
}