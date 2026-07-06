package com.enterprise.banking.tests;

import com.enterprise.banking.ai.dom.model.DOMElement;
import com.enterprise.banking.ai.dom.service.DOMExtractionService;
import com.enterprise.banking.ai.mapper.ActionMappingEngine;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Validates the Action Mapping Engine by generating DOM metadata
 * and triggering the intelligent code generation protocol.
 */
public class MappingEngineTest {

    @Test
    public void testMappingEnginePipeline() throws Exception {
        System.out.println("==================================================");
        System.out.println("      ACTION MAPPING ENGINE - TEST START          ");
        System.out.println("==================================================");

        // 1. Extract the DOM Ground Truth Repository
        DOMExtractionService domService = new DOMExtractionService();
        
        // FIXED URL: Changed from login.htm to index.htm
        String targetUrl = "https://parabank.parasoft.com/parabank/index.htm";
        
        List<DOMElement> domRepository = domService.executeDomAnalysis(targetUrl);

        // 2. Execute the Intelligent Mapping Protocol
        ActionMappingEngine mappingEngine = new ActionMappingEngine();
        mappingEngine.executeMappingProtocol(domRepository);

        System.out.println("==================================================");
        System.out.println("      ACTION MAPPING ENGINE - TEST END            ");
        System.out.println("==================================================");
    }
}