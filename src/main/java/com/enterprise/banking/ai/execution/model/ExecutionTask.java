package com.enterprise.banking.ai.execution.model;

import com.enterprise.banking.ai.model.ScenarioPriority;
import java.util.UUID;

/**
 * Represents a discrete, actionable unit of work within an Execution Plan,
 * mapping directly to a specific class or method in the existing automation framework.
 */
public class ExecutionTask {

    private String taskId;
    private String taskName;
    private ExecutionTarget targetType;
    private String mappedClass;
    private String mappedMethod;
    private ScenarioPriority priority;
    private ExecutionStatus status;

    /**
     * Default constructor initializing unique identifiers and default status.
     */
    public ExecutionTask() {
        this.taskId = "TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.status = ExecutionStatus.PENDING;
    }

    /**
     * Parameterized constructor for direct task instantiation.
     *
     * @param taskName     The descriptive name of the task
     * @param targetType   The target execution layer
     * @param mappedClass  The fully qualified class name in the existing framework
     * @param mappedMethod The specific method to invoke, if applicable
     * @param priority     The execution priority
     */
    public ExecutionTask(String taskName, ExecutionTarget targetType, String mappedClass, String mappedMethod, ScenarioPriority priority) {
        this();
        this.taskName = taskName;
        this.targetType = targetType;
        this.mappedClass = mappedClass;
        this.mappedMethod = mappedMethod;
        this.priority = priority;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ExecutionTarget getTargetType() {
        return targetType;
    }

    public void setTargetType(ExecutionTarget targetType) {
        this.targetType = targetType;
    }

    public String getMappedClass() {
        return mappedClass;
    }

    public void setMappedClass(String mappedClass) {
        this.mappedClass = mappedClass;
    }

    public String getMappedMethod() {
        return mappedMethod;
    }

    public void setMappedMethod(String mappedMethod) {
        this.mappedMethod = mappedMethod;
    }

    public ScenarioPriority getPriority() {
        return priority;
    }

    public void setPriority(ScenarioPriority priority) {
        this.priority = priority;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }
}