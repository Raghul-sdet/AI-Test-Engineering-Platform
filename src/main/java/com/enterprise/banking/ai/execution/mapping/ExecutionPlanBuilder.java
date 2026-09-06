package com.enterprise.banking.ai.execution.mapping;

import com.enterprise.banking.ai.execution.model.ExecutionPlan;
import com.enterprise.banking.ai.execution.model.ExecutionTarget;
import com.enterprise.banking.ai.execution.model.ExecutionTask;
import com.enterprise.banking.ai.execution.model.MappedScenario;
import com.enterprise.banking.ai.model.GeneratedStep;

/**
 * Follows the Builder pattern to systematically construct a complex ExecutionPlan
 * from a fully resolved MappedScenario.
 */
public class ExecutionPlanBuilder {

    private ExecutionPlan executionPlan;

    /**
     * Initializes a new builder session.
     */
    public ExecutionPlanBuilder() {
        this.executionPlan = new ExecutionPlan();
    }

    /**
     * Initializes a new builder session.
     * @return A new instance of ExecutionPlanBuilder
     */
    public static ExecutionPlanBuilder start() {
        return new ExecutionPlanBuilder();
    }

    /**
     * Attaches the primary mapped scenario to the execution plan.
     *
     * @param mappedScenario The mapped scenario object
     * @return The current builder instance
     */
    public ExecutionPlanBuilder withMappedScenario(MappedScenario mappedScenario) {
        if (mappedScenario == null || mappedScenario.getScenario() == null) {
            throw new IllegalArgumentException("MappedScenario cannot be null.");
        }
        this.executionPlan.setMappedScenario(mappedScenario);
        this.executionPlan.setPriority(mappedScenario.getScenario().getPriority());
        this.executionPlan.setMappedTestClass(mappedScenario.getMappedTestClass());
        this.executionPlan.setMappedPageObject(mappedScenario.getMappedPageObject());
        return this;
    }

    /**
     * Translates generated scenario steps into executable target tasks.
     *
     * @return The current builder instance
     */
    public ExecutionPlanBuilder buildTasks() {
        MappedScenario ms = this.executionPlan.getMappedScenario();
        if (ms == null || ms.getScenario().getSteps() == null) {
            return this;
        }

        // Add initialization task tied to Page Object
        this.executionPlan.addTask(new ExecutionTask(
                "Initialize Page: " + extractSimpleName(ms.getMappedPageObject()),
                ExecutionTarget.PAGE_OBJECT,
                ms.getMappedPageObject(),
                "init",
                ms.getScenario().getPriority()
        ));

        // Convert functional steps to Execution Tasks mapping to the Test Class
        for (GeneratedStep step : ms.getScenario().getSteps()) {
            this.executionPlan.addTask(new ExecutionTask(
                    "Execute Step " + step.getStepNumber() + ": " + truncate(step.getAction(), 30),
                    ExecutionTarget.TEST_CLASS,
                    ms.getMappedTestClass(),
                    "executeAutoMappedStep",
                    ms.getScenario().getPriority()
            ));
        }

        return this;
    }

    /**
     * Finalizes and retrieves the fully constructed execution plan.
     *
     * @return The finalized ExecutionPlan
     */
    public ExecutionPlan getPlan() {
        return this.executionPlan;
    }

    private String extractSimpleName(String fullyQualifiedName) {
        if (fullyQualifiedName == null || !fullyQualifiedName.contains(".")) {
            return fullyQualifiedName;
        }
        return fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.') + 1);
    }
    
    private String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() <= length ? text : text.substring(0, length) + "...";
    }
}