package com.enterprise.banking.tests;

import com.enterprise.banking.config.SiteProfileLoader;
import com.enterprise.banking.workflow.GenericWorkflowEngine;
import org.testng.annotations.Test;

public class GenericWorkflowTest extends BaseTest {

    @Test
    public void testGenericWorkflow() {
        String workflowName = System.getProperty("workflow.name", "login");
        
        // BaseTest initializes driver based on config.properties, but we 
        // will rely on its driver object.
        SiteProfileLoader loader = new SiteProfileLoader();
        GenericWorkflowEngine engine = new GenericWorkflowEngine();
        
        engine.execute(driver, loader, workflowName);
    }
}
