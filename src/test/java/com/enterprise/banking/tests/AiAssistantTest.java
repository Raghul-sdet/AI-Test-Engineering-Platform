package com.enterprise.banking.tests;

import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.service.AiDatabasePersister;
import com.enterprise.banking.ai.service.AiTestOrchestrator;
import com.enterprise.banking.ai.export.DatabaseToExcelExporter;
import com.enterprise.banking.ai.generator.CodeGenerationEngine;
import org.testng.annotations.Test;

public class AiAssistantTest {

    @Test
    public void runFullAiAutomationFlow() throws Exception {
        System.out.println("==================================================");
        System.out.println("   ENTERPRISE AI QA FRAMEWORK - EXECUTION START   ");
        System.out.println("==================================================");

        // Initialize Core Generation Components (Updated for Enterprise Architecture)
        AiTestOrchestrator orchestrator = new AiTestOrchestrator();
        AiDatabasePersister dbPersister = new AiDatabasePersister();

        // 1. AI Generation Phase
        String featureToTest = "SWIFT Cross-Border Fund Transfer";
        System.out.println("[AI-ENGINE] Generating comprehensive test plan for: " + featureToTest + "...");
        TestPlan plan = orchestrator.buildCompleteTestPlan(featureToTest);
        System.out.println("[AI-ENGINE] Generation Complete! Total Scenarios: " + plan.scenarios().size());

        // 2. Persistence Phase
        System.out.println("[DB-ENGINE] Saving generated AI test artifacts to H2 Database...");
        dbPersister.persistTestPlan(plan);

        // 3. Export Phase (Excel Workbook Generation)
        System.out.println("\n--- EXPORTING DATA FROM DATABASE TO EXCEL ---");
        DatabaseToExcelExporter exporter = new DatabaseToExcelExporter();
        String excelPath = "AI_Test_Design.xlsx";
        // Fixed: Passing both plan and path as required by the updated exporter
        exporter.exportToExcel(plan, excelPath);

        // 4. Code Generation Phase (Excel Workbook to Java Selenium Source Files)
        System.out.println("\n--- STAGE 1: AI SELENIUM CODE GENERATION ---");
        System.out.println("[CODE-ENGINE] Reading test design from: " + excelPath + "...");
        CodeGenerationEngine codeEngine = new CodeGenerationEngine();
        codeEngine.runGenerationPipeline(excelPath);
        System.out.println("[CODE-ENGINE] Code generation processing completed successfully.");

        System.out.println("==================================================");
        System.out.println("   ENTERPRISE AI QA FRAMEWORK - EXECUTION END     ");
        System.out.println("==================================================");
    }
}