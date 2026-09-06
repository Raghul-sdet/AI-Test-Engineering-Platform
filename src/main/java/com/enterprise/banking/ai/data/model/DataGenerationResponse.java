package com.enterprise.banking.ai.data.model;

/**
 * A standard DTO wrapping the final generated dataset along with system status flags.
 */
public class DataGenerationResponse {

    private String requestId;
    private GeneratedDataset dataset;
    private boolean isSuccessful;
    private String statusMessage;

    /**
     * Default constructor.
     */
    public DataGenerationResponse() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public GeneratedDataset getDataset() {
        return dataset;
    }

    public void setDataset(GeneratedDataset dataset) {
        this.dataset = dataset;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}