package com.enterprise.banking.ai.model;

/**
 * Represents a specific risk identified within a business requirement.
 */
public class RequirementRisk {

    private String riskType;
    private String severity;
    private String description;
    private String mitigation;

    /**
     * Default constructor.
     */
    public RequirementRisk() {
    }

    /**
     * Parameterized constructor.
     *
     * @param riskType    Category or type of the risk
     * @param severity    Severity level (e.g., Critical, High, Medium, Low)
     * @param description Detailed description of the risk
     * @param mitigation  Suggested mitigation strategy
     */
    public RequirementRisk(String riskType, String severity, String description, String mitigation) {
        this.riskType = riskType;
        this.severity = severity;
        this.description = description;
        this.mitigation = mitigation;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMitigation() {
        return mitigation;
    }

    public void setMitigation(String mitigation) {
        this.mitigation = mitigation;
    }
}