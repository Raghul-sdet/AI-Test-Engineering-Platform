package com.enterprise.banking.ai.execution;

import org.testng.TestNG;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Programmatically triggers the execution of dynamically compiled TestNG classes,
 * ensuring they run within the isolated AI sandbox using a custom dynamic ClassLoader.
 */
public class GeneratedTestRunner {

    /**
     * Executes the specified compiled test class using TestNG.
     *
     * @param fullyQualifiedClassName The name of the generated test class.
     * @return The execution telemetry report.
     */
    public ExecutionReport executeSandboxedTest(String fullyQualifiedClassName) {
        System.out.println("\n[EXECUTION-ENGINE] Initializing isolated TestNG environment...");
        
        TestNG testng = new TestNG();
        
        try {
            // 1. Point a custom ClassLoader to our "generated-code" directory
            File classesDir = new File("generated-code");
            
            // We set the parent classloader so it can still find BaseTest and Selenium libraries
            URLClassLoader customClassLoader = URLClassLoader.newInstance(
                    new URL[] { classesDir.toURI().toURL() },
                    this.getClass().getClassLoader() 
            );
            
            // 2. Dynamically load the generated Test class into JVM memory
            Class<?> testClass = Class.forName(fullyQualifiedClassName, true, customClassLoader);
            
            // 3. Pass the loaded class directly to TestNG (Bypassing XML constraints)
            testng.setTestClasses(new Class[] { testClass });

            // Attach telemetry listener
            AiFailureListener aiListener = new AiFailureListener();
            testng.addListener(aiListener);
            
            // Execute the sandbox suite
            System.out.println("[EXECUTION-ENGINE] Triggering execution for: " + fullyQualifiedClassName);
            testng.run();
            
            return aiListener.getLastReport();
            
        } catch (Exception e) {
            System.err.println("[EXECUTION-ENGINE-ERROR] Failed to dynamically load class: " + e.getMessage());
            return new ExecutionReport(fullyQualifiedClassName, false, e.getMessage(), null, null, null, null);
        }
    }
}