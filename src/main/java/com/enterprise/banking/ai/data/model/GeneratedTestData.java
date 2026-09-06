package com.enterprise.banking.ai.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents an aggregated, single-record data payload designed to satisfy 
 * the inputs of a specific business transaction or form submission.
 */
public class GeneratedTestData {

    private String dataRecordId;
    private String relatedContext;
    private final Map<String, GeneratedField> fields;

    /**
     * Default constructor initializing unique identifier and data map.
     */
    public GeneratedTestData() {
        this.dataRecordId = "DATA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.fields = new HashMap<>();
    }

    public String getDataRecordId() {
        return dataRecordId;
    }

    public void setDataRecordId(String dataRecordId) {
        this.dataRecordId = dataRecordId;
    }

    public String getRelatedContext() {
        return relatedContext;
    }

    public void setRelatedContext(String relatedContext) {
        this.relatedContext = relatedContext;
    }

    /**
     * Adds or overwrites a generated field in the dataset.
     *
     * @param field The field to inject
     */
    public void addField(GeneratedField field) {
        if (field != null && field.getFieldName() != null) {
            this.fields.put(field.getFieldName(), field);
        }
    }

    /**
     * Retrieves a specific field by its logical name.
     *
     * @param fieldName The name of the field to retrieve
     * @return GeneratedField, or null if it doesn't exist
     */
    public GeneratedField getField(String fieldName) {
        return this.fields.get(fieldName);
    }

    /**
     * Retrieves all fields contained in this data record.
     *
     * @return List of all generated fields
     */
    public List<GeneratedField> getAllFields() {
        return new ArrayList<>(fields.values());
    }

    /**
     * Determines if this data record contains any explicitly generated negative path values.
     *
     * @return true if negative fields exist, false otherwise
     */
    public boolean containsNegativeData() {
        return fields.values().stream().filter(f -> f != null).anyMatch(f -> f.isNegativePath());
    }
}