package com.enterprise.banking.ai.execution.runtime;

import com.enterprise.banking.ai.exception.ExecutionOrchestratorException;
import com.enterprise.banking.ai.execution.listener.ExecutionListener;
import org.testng.TestNG;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Programmatically instantiates and executes TestNG dynamically for designated automation classes,
 * extracting real-time results back to the AI orchestration pipeline.
 */
public class RuntimeExecutionEngine {

    private static final Logger LOGGER = Logger.getLogger(RuntimeExecutionEngine.class.getName());

    /**
     * Executes the target fully qualified class name using TestNG dynamically.
     *
     * @param fullyQualifiedClassName The class path to execute
     * @return Aggregated statistics from the run
     * @throws ExecutionOrchestratorException if class loading or execution fails
     */
    public RuntimeStatistics executeClass(String fullyQualifiedClassName) {
        if (fullyQualifiedClassName == null || fullyQualifiedClassName.isEmpty()) {
            throw new ExecutionOrchestratorException("Target test class name cannot be null or empty.");
        }

        LOGGER.info("Configuring dynamic TestNG execution for class: " + fullyQualifiedClassName);
        RuntimeStatistics statistics = new RuntimeStatistics();
        
        try {
            Class<?> targetClass = Class.forName(fullyQualifiedClassName);
            
            TestNG testng = new TestNG();
            testng.setUseDefaultListeners(false); // Prevents system.exit on failure
            testng.setTestClasses(new Class[]{targetClass});
            
            ExecutionListener listener = new ExecutionListener(statistics);
            testng.addListener(listener);

            LOGGER.info("Triggering TestNG run programmatically...");
            testng.run();
            LOGGER.info("TestNG dynamic run completed.");

        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Mapped test class not found in classpath: {0}", fullyQualifiedClassName);
            throw new ExecutionOrchestratorException("Failed to locate target test class: " + fullyQualifiedClassName, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during TestNG programmatic execution.", e);
            throw new ExecutionOrchestratorException("Dynamic TestNG engine encountered a critical error.", e);
        }

        return statistics;
    }
}