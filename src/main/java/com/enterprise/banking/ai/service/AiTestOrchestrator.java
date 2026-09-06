package com.enterprise.banking.ai.service;

import com.enterprise.banking.ai.requirement.RequirementPlanner;
import com.enterprise.banking.ai.dom.model.DOMElement;
import com.enterprise.banking.ai.dom.service.DOMExtractionService;
import com.enterprise.banking.ai.export.DatabaseToExcelExporter;
import com.enterprise.banking.ai.generator.SeleniumCodeGenerator;
import com.enterprise.banking.ai.execution.ExecutionCoordinator;
import com.enterprise.banking.ai.execution.ExecutionReport;
import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.model.TestScenario;
import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.exception.AiExtensionException;
import com.enterprise.banking.ai.provider.AiProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        this.requirementPlanner = new RequirementPlanner(resolveDefaultProvider());
        this.scenarioGeneratorService = new AiScenarioGeneratorService();
        this.testCaseGeneratorService = new AiTestCaseGeneratorService();
        this.databasePersister = new AiDatabasePersister();
        this.excelExporter = new DatabaseToExcelExporter();
        this.seleniumCodeGenerator = new SeleniumCodeGenerator();
        this.executionCoordinator = new ExecutionCoordinator();
    }

    /**
     * Resolves the AI provider to use when no explicit provider is injected.
     * Previously this always instantiated {@link OpenAiProvider} directly, which throws
     * immediately if the AI_API_KEY environment variable / ai.api.key system property is
     * not set - meaning the whole pipeline (and any test that uses the no-arg constructor,
     * e.g. EndToEndAiExecution, AiAssistantTest) failed before doing any work.
     * <p>
     * Now: if a real key is configured, we use the live OpenAI provider. Otherwise we fall
     * back to Ollama local inference so the rest of the pipeline (persistence, Excel export, 
     * code generation, execution, reporting) can be exercised offline with zero API cost.
     *
     * @return a usable AiProvider, never null, never throws.
     */
    private static AiProvider resolveDefaultProvider() {
        return com.enterprise.banking.ai.provider.AiProviderFactory.createProvider();
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

            // Attach the generated scenarios and test cases back onto the TestPlan.
            // RequirementPlanner.buildTestPlan() only sets the plan's own fields (planId,
            // originalRequirement, aiContext, etc.) - it has no knowledge of the scenarios/test
            // cases generated in stages 2/3 above, since those run through separate services.
            // Without this, testPlan.scenarios() stays the empty list from TestPlan's default
            // constructor forever, which DatabaseToExcelExporter reads directly (it does NOT
            // re-query the database) - so the Excel report was always generated empty
            // ("No scenarios found in TestPlan ID: ... Report will be largely empty.")
            // regardless of how many scenarios/test cases were actually generated and persisted.
            testPlan.setScenarios(testScenarios);
            Map<String, List<TestCase>> testCasesByScenarioId = new HashMap<>();
            for (TestCase testCase : testCases) {
                testCasesByScenarioId
                        .computeIfAbsent(testCase.getScenarioId(), key -> new java.util.ArrayList<>())
                        .add(testCase);
            }
            testPlan.setTestCasesByScenarioId(testCasesByScenarioId);

            // Stage 4: Database Persistence
            LOGGER.info("Stage 4/7: Persisting generated assets to internal H2 Database");
            databasePersister.saveTestPlan(testPlan);
            databasePersister.saveTestScenarios(testScenarios);
            databasePersister.saveTestCases(testCases);

            // Stage 5: Enterprise Excel Generation (Matches exportToExcel(TestPlan, String) signature)
            LOGGER.info("Stage 5/7: Exporting traceability matrix to Enterprise Excel");
            String excelFilePath = workspacePath + File.separator + "reports" + File.separator + "Professional_Enterprise_Report.xlsx";
            File excelReport = excelExporter.exportToExcel(testPlan, excelFilePath);
            if (excelReport != null) {
                LOGGER.info("Excel report generated successfully at: {}", excelReport.getAbsolutePath());
            }

            // Stage 6: Selenium Java Code Generation
            LOGGER.info("Stage 6/7: Generating Production-Ready Selenium WebDriver Code");
            String codeGenerationPath = workspacePath + File.separator + "generated-code";

            // DOM discovery against the configured target site (see TargetSiteConfig) so the
            // generated code below binds to REAL elements instead of being a content-free
            // stub. Previously SeleniumCodeGenerator never looked at either the test cases'
            // actual steps or any real page - every generated test method was an identical
            // no-op that always "passed" regardless of what the AI generated. Failure here is
            // non-fatal: an empty element list just means every step gets skipped with a
            // comment in the generated code, rather than the whole pipeline aborting.
            String targetUrl = TargetSiteConfig.resolveTargetUrl();
            List<DOMElement> domRepository;
            try {
                domRepository = new DOMExtractionService().executeDomAnalysis(targetUrl);
            } catch (Exception domException) {
                LOGGER.warn("DOM discovery failed for {} - generated steps will be skip comments. Cause: {}",
                        targetUrl, domException.getMessage());
                domRepository = java.util.Collections.emptyList();
            }

            File generatedCodeLocation = seleniumCodeGenerator.generateSeleniumCode(
                    testCases, codeGenerationPath, domRepository, targetUrl);

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