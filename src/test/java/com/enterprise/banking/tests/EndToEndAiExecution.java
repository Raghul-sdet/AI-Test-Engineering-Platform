package com.enterprise.banking.tests;

import com.enterprise.banking.ai.dom.model.DOMElement;
import com.enterprise.banking.ai.dom.service.DOMExtractionService;
import com.enterprise.banking.ai.execution.ExecutionCoordinator;
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
        String excelPath = "AI_Test_Design.xlsx";
        exporter.exportToExcel(plan, excelPath); 

        // 3. DOM Extraction
        DOMExtractionService domService = new DOMExtractionService();
        List<DOMElement> domRepository = domService.executeDomAnalysis("https://parabank.parasoft.com/parabank/index.htm");
        
        // 4. Action Mapping Engine
        ActionMappingEngine mappingEngine = new ActionMappingEngine();
        mappingEngine.executeMappingProtocol(domRepository);
        
        // 5. Execution Integration
        ExecutionCoordinator coordinator = new ExecutionCoordinator();
        coordinator.runExecutionPipeline("generated-code", "com.enterprise.generated.tests.IntelligentLoginTest");

        System.out.println("==================================================");
        System.out.println("      AI FRAMEWORK - END TO END EXECUTION END     ");
        System.out.println("==================================================");
    }
}