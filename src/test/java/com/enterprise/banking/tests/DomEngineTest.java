package com.enterprise.banking.tests;

import com.enterprise.banking.ai.dom.service.DOMExtractionService;
import org.testng.annotations.Test;

/**
 * Validates the DOM Intelligence Engine by executing an extraction
 * against a live web application.
 */
public class DomEngineTest {

    @Test
    public void testDomExtractionPipeline() {
        System.out.println("==================================================");
        System.out.println("       DOM INTELLIGENCE ENGINE - TEST START       ");
        System.out.println("==================================================");

        // Initialize the extraction service
        DOMExtractionService domService = new DOMExtractionService();
        
        // Target URL for analysis (Using ParaBank Registration Page)
        String targetUrl = "https://parabank.parasoft.com/parabank/register.htm";
        
        // Execute the analysis
        domService.executeDomAnalysis(targetUrl);

        System.out.println("==================================================");
        System.out.println("       DOM INTELLIGENCE ENGINE - TEST END         ");
        System.out.println("==================================================");
    }
}