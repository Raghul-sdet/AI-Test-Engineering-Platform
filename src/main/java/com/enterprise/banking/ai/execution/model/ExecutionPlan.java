package com.enterprise.banking.ai.execution.model;

import com.enterprise.banking.ai.model.ScenarioPriority;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents the comprehensive blueprint for executing an AI-generated scenario
 * using existing automation framework assets.
 */
public class ExecutionPlan {

    private String planId;
    private LocalDateTime generatedTime;
    private MappedScenario mappedScenario;
    private ScenarioPriority priority;
    private String mappedTestClass;
    private String mappedPageObject;
    private int executionOrder;
    private ExecutionStatus status;
    private List<ExecutionTask> tasks;

    /**
     * Default constructor initializing state and task collections.
     */
    public ExecutionPlan() {
        this.planId = "PLAN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.generatedTime = LocalDateTime.now();
        this.status = ExecutionStatus.PENDING;
        this.tasks = new ArrayList<>();
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public LocalDateTime getGeneratedTime() {
        return generatedTime;
    }

    public void setGeneratedTime(LocalDateTime generatedTime) {
        this.generatedTime = generatedTime;
    }

    public MappedScenario getMappedScenario() {
        return mappedScenario;
    }

    public void setMappedScenario(MappedScenario mappedScenario) {
        this.mappedScenario = mappedScenario;
    }

    public ScenarioPriority getPriority() {
        return priority;
    }

    public void setPriority(ScenarioPriority priority) {
        this.priority = priority;
    }

    public String getMappedTestClass() {
        return mappedTestClass;
    }

    public void setMappedTestClass(String mappedTestClass) {
        this.mappedTestClass = mappedTestClass;
    }

    public String getMappedPageObject() {
        return mappedPageObject;
    }

    public void setMappedPageObject(String mappedPageObject) {
        this.mappedPageObject = mappedPageObject;
    }

    public int getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(int executionOrder) {
        this.executionOrder = executionOrder;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public List<ExecutionTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<ExecutionTask> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds an execution task to the plan.
     *
     * @param task The task to append
     */
    public void addTask(ExecutionTask task) {
        if (task != null) {
            this.tasks.add(task);
        }
    }
}