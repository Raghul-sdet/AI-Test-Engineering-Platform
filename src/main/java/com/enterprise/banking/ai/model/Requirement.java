package com.enterprise.banking.ai.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The master model representing a fully analyzed business requirement.
 */
public class Requirement {

    private String id;
    private String title;
    private String description;
    private List<RequirementFeature> features;
    private RequirementPriority priority;
    private RequirementCoverage coverage;
    private List<RequirementRisk> risks;
    private LocalDateTime createdDate;

    /**
     * Default constructor initializing collections.
     */
    public Requirement() {
        this.features = new ArrayList<>();
        this.risks = new ArrayList<>();
        this.createdDate = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RequirementFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<RequirementFeature> features) {
        this.features = features;
    }

    public RequirementPriority getPriority() {
        return priority;
    }

    public void setPriority(RequirementPriority priority) {
        this.priority = priority;
    }

    public RequirementCoverage getCoverage() {
        return coverage;
    }

    public void setCoverage(RequirementCoverage coverage) {
        this.coverage = coverage;
    }

    public List<RequirementRisk> getRisks() {
        return risks;
    }

    public void setRisks(List<RequirementRisk> risks) {
        this.risks = risks;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}