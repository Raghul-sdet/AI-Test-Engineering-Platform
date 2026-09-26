package com.enterprise.banking.config;

import java.util.List;

public class WorkflowConfig {
    private List<WorkflowStep> steps;

    public List<WorkflowStep> getSteps() {
        return steps;
    }

    public void setSteps(List<WorkflowStep> steps) {
        this.steps = steps;
    }
}
