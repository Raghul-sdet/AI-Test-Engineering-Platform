package com.enterprise.banking.ai.data.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Encapsulates a collection of generated test data records, facilitating
 * bulk test execution, data-driven frameworks, and parallel testing.
 */
public class GeneratedDataset {

    private String datasetId;
    private String scenarioId;
    private LocalDateTime generatedTime;
    private final List<GeneratedTestData> records;

    /**
     * Default constructor initializing dataset identity and collection.
     */
    public GeneratedDataset() {
        this.datasetId = "DSET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.generatedTime = LocalDateTime.now();
        this.records = new ArrayList<>();
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public LocalDateTime getGeneratedTime() {
        return generatedTime;
    }

    public void setGeneratedTime(LocalDateTime generatedTime) {
        this.generatedTime = generatedTime;
    }

    /**
     * Adds a single data record to the bulk dataset.
     *
     * @param record The generated test data payload
     */
    public void addRecord(GeneratedTestData record) {
        if (record != null) {
            this.records.add(record);
        }
    }

    /**
     * Returns a safe copy of all records in this dataset.
     *
     * @return List of GeneratedTestData
     */
    public List<GeneratedTestData> getRecords() {
        return new ArrayList<>(records);
    }

    /**
     * Returns the total count of records generated in this set.
     *
     * @return Size of the dataset
     */
    public int getSize() {
        return records.size();
    }
}