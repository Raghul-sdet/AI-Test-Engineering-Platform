package com.enterprise.banking.ai.service;

import com.enterprise.banking.ai.requirement.RequirementPlanner;
import com.enterprise.banking.ai.export.DatabaseToExcelExporter;
import com.enterprise.banking.ai.generator.SeleniumCodeGenerator;
import com.enterprise.banking.ai.execution.ExecutionCoordinator;
import com.enterprise.banking.ai.execution.ExecutionReport;
import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.model.TestScenario;
import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.exception.AiExtensionException;
import com.enterprise.banking.ai.provider.OpenAiProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Orchestrates the end-to-end AI Quality Assurance pipeline for Phase 1.
 * Matches existing test executions and acts as the central facade for the framework.
 */
public class AiTestOrchestrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AiTestOrchestrator.class);

    private final RequirementPlanner requirementPlanner;
    private final AiScenarioGeneratorService scenarioGeneratorService;
    private final AiTestCaseGeneratorService testCaseGeneratorService;
    private final AiDatabasePersister databasePersister;
    private final DatabaseToExcelExporter excelExporter;
    private final SeleniumCodeGenerator seleniumCodeGenerator;
    private final ExecutionCoordinator executionCoordinator;

    /**
     * Default constructor required by existing test classes (e.g., AiAssistantTest).
     * Automatically wires the standard enterprise implementations.
     */
    public AiTestOrchestrator() {
        this.requirementPlanner = new RequirementPlanner(new OpenAiProvider());
        this.scenarioGeneratorService = new AiScenarioGeneratorService();
        this.testCaseGeneratorService = new AiTestCaseGeneratorService();
        this.databasePersister = new AiDatabasePersister();
        this.excelExporter = new DatabaseToExcelExporter();
        this.seleniumCodeGenerator = new SeleniumCodeGenerator();
        this.executionCoordinator = new ExecutionCoordinator();
    }

    /**
     * Dependency Injection constructor to maintain SOLID principles for custom testing.
     *
     * @param requirementPlanner       Analyzes raw requirements.
     * @param scenarioGeneratorService Generates abstract scenarios.
     * @param testCaseGeneratorService Generates concrete test cases.
     * @param databasePersister        Persists data to H2.
     * @param excelExporter            Exports data to Excel.
     * @param seleniumCodeGenerator    Generates Selenium code.
     * @param executionCoordinator     Executes tests and builds reports.
     */
    public AiTestOrchestrator(
            RequirementPlanner requirementPlanner,
            AiScenarioGeneratorService scenarioGeneratorService,
            AiTestCaseGeneratorService testCaseGeneratorService,
            AiDatabasePersister databasePersister,
            DatabaseToExcelExporter excelExporter,
            SeleniumCodeGenerator seleniumCodeGenerator,
            ExecutionCoordinator executionCoordinator) {
        
        this.requirementPlanner = Objects.requireNonNull(requirementPlanner, "RequirementPlanner cannot be null");
        this.scenarioGeneratorService = Objects.requireNonNull(scenarioGeneratorService, "ScenarioGeneratorService cannot be null");
        this.testCaseGeneratorService = Objects.requireNonNull(testCaseGeneratorService, "TestCaseGeneratorService cannot be null");
        this.databasePersister = Objects.requireNonNull(databasePersister, "DatabasePersister cannot be null");
        this.excelExporter = Objects.requireNonNull(excelExporter, "DatabaseToExcelExporter cannot be null");
        this.seleniumCodeGenerator = Objects.requireNonNull(seleniumCodeGenerator, "SeleniumCodeGenerator cannot be null");
        this.executionCoordinator = Objects.requireNonNull(executionCoordinator, "ExecutionCoordinator cannot be null");
    }

    /**
     * Executes the complete Phase-1 pipeline of the Enterprise AI Testing product.
     *
     * @param requirementText The raw functional requirement or user story.
     * @return The generated TestPlan containing the execution details.
     * @throws AiExtensionException if any stage of the pipeline fails.
     */
    public TestPlan buildCompleteTestPlan(String requirementText) {
        if (requirementText == null || requirementText.trim().isEmpty()) {
            throw new IllegalArgumentException("Requirement text must not be null or empty.");
        }

        String workspacePath = System.getProperty("user.dir") + File.separator + "target";
        LOGGER.info("Initiating Enterprise AI QA Automation Framework - Phase 1 Pipeline");

        try {
            // Stage 1: Requirement Analysis
            LOGGER.info("Stage 1/7: Analyzing Requirements");
            TestPlan testPlan = requirementPlanner.analyzeRequirement(requirementText);

            // Stage 2: AI Scenario Generation (Matches generateScenarios(String) signature)
            LOGGER.info("Stage 2/7: Generating AI Test Scenarios");
            List<TestScenario> testScenarios = scenarioGeneratorService.generateScenarios(requirementText);

            // Stage 3: AI Test Case & Step Generation
            LOGGER.info("Stage 3/7: Generating AI Test Cases and Execution Steps");
            List<TestCase> testCases = testCaseGeneratorService.generateTestCasesWithSteps(testScenarios);

            // Stage 4: Database Persistence
            LOGGER.info("Stage 4/7: Persisting generated assets to internal H2 Database");
            databasePersister.saveTestPlan(testPlan);
            databasePersister.saveTestScenarios(testScenarios);
            databasePersister.saveTestCases(testCases);

            // Stage 5: Enterprise Excel Generation (Matches exportToExcel(TestPlan, String) signature)
            LOGGER.info("Stage 5/7: Exporting traceability matrix to Enterprise Excel");
            String excelFilePath = workspacePath + File.separator + "reports" + File.separator + "AiTraceabilityReport.xlsx";
            File excelReport = excelExporter.exportToExcel(testPlan, excelFilePath);
            if (excelReport != null) {
                LOGGER.info("Excel report generated successfully at: {}", excelReport.getAbsolutePath());
            }

            // Stage 6: Selenium Java Code Generation
            LOGGER.info("Stage 6/7: Generating Production-Ready Selenium WebDriver Code");
            String codeGenerationPath = workspacePath + File.separator + "generated-code";
            File generatedCodeLocation = seleniumCodeGenerator.generateSeleniumCode(testCases, codeGenerationPath);

            // Stage 7: Compilation, Execution and Reporting
            LOGGER.info("Stage 7/7: Compiling generated code, executing TestNG suite, and generating Extent/Allure Reports");
            ExecutionReport finalReport = executionCoordinator.compileAndExecute(generatedCodeLocation);

            if (finalReport != null) {
                LOGGER.info("Phase 1 Pipeline executed successfully. Total tests passed: {}", finalReport.getPassCount());
            }

            return testPlan;

        } catch (Exception exception) {
            LOGGER.error("Fatal error occurred during Phase-1 AI Pipeline execution: {}", exception.getMessage(), exception);
            throw new AiExtensionException("Failed to complete the AI Test Orchestration pipeline", exception);
        }
    }
}