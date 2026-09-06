package com.enterprise.banking.tests;

import com.enterprise.banking.ai.dom.model.DOMElement;
import com.enterprise.banking.ai.dom.service.DOMExtractionService;
import com.enterprise.banking.ai.export.DatabaseToExcelExporter;
import com.enterprise.banking.ai.mapper.ActionMappingEngine;
import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.service.AiDatabasePersister;
import com.enterprise.banking.ai.service.AiTestOrchestrator;
import org.testng.annotations.Test;
import java.util.List;

public class EndToEndAiExecution {

    @Test
    public void runFullEndToEndPipeline() throws Exception {
        System.out.println("==================================================");
        System.out.println("      AI FRAMEWORK - END TO END EXECUTION START   ");
        System.out.println("==================================================");

        // 1. AI Generation & DB Persistance (Fixed Constructor)
        AiTestOrchestrator orchestrator = new AiTestOrchestrator();
        AiDatabasePersister dbPersister = new AiDatabasePersister();

        TestPlan plan = orchestrator.buildCompleteTestPlan("SWIFT Cross-Border Fund Transfer");
        dbPersister.persistTestPlan(plan);

        // 2. Export to Excel (Fixed Parameters)
        DatabaseToExcelExporter exporter = new DatabaseToExcelExporter();
        String excelPath = System.getProperty("user.dir") + java.io.File.separator + "target" + java.io.File.separator + "reports" + java.io.File.separator + "Professional_Enterprise_Report.xlsx";
        exporter.exportToExcel(plan, excelPath); 

        // Single source of truth for "which website are we testing" - same config.properties
        // key BaseTest reads. Change uiBaseUrl there and the whole pipeline (DOM discovery,
        // action mapping, generated test code) points at a different site.
        String targetUrl = com.enterprise.banking.utils.ConfigReader.getProperty("uiBaseUrl");
        if (targetUrl == null || targetUrl.isBlank()) {
            targetUrl = com.enterprise.banking.utils.ConfigReader.getProperty("baseUrl");
        }

        // 3. DOM Extraction
        DOMExtractionService domService = new DOMExtractionService();
        List<DOMElement> domRepository = domService.executeDomAnalysis(targetUrl);
        
        // 4. Action Mapping Engine
        ActionMappingEngine mappingEngine = new ActionMappingEngine();
        mappingEngine.executeMappingProtocol(domRepository, targetUrl);
        
        // Note: compilation + execution of the AI-generated Selenium code already happens
        // inside orchestrator.buildCompleteTestPlan() above (its internal Stage 6/7 - see
        // AiTestOrchestrator). A separate "Stage 5" call to
        // ExecutionCoordinator.runExecutionPipeline("generated-code", <fake class name>) used
        // to live here, but "generated-code" and the class-name string were never wired to the
        // real, dynamically-timestamped file the orchestrator just generated (e.g.
        // AiGeneratedWebAutomationTest_1785991326999.java) - it was placeholder/dead test code
        // left over from before the orchestrator absorbed this responsibility, and could not
        // succeed no matter what value it pointed at.

        System.out.println("==================================================");
        System.out.println("      AI FRAMEWORK - END TO END EXECUTION END     ");
        System.out.println("==================================================");
    }
}