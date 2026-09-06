package com.enterprise.banking.ai.model;

import java.util.Objects;

/**
 * Represents a distinct feature identified within a business requirement.
 */
public class RequirementFeature {
    
    private String featureId;
    private String featureName;
    private String moduleName;
    private String description;

    public RequirementFeature() {
    }

    public RequirementFeature(String featureId, String featureName, String moduleName, String description) {
        this.featureId = featureId;
        this.featureName = featureName;
        this.moduleName = moduleName;
        this.description = description;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequirementFeature that = (RequirementFeature) o;
        return Objects.equals(featureId, that.featureId) &&
               Objects.equals(featureName, that.featureName) &&
               Objects.equals(moduleName, that.moduleName) &&
               Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureId, featureName, moduleName, description);
    }
}