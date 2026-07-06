package com.enterprise.banking.ai.service;

import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.model.TestScenario;
import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.exception.AiExtensionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Handles the persistence of AI-generated QA artifacts into the embedded H2 database.
 * Establishes standard JDBC connections to ensure data traceability, enabling 
 * comprehensive reporting and dashboard generation.
 */
public class AiDatabasePersister {

    private static final Logger LOGGER = LoggerFactory.getLogger(AiDatabasePersister.class);
    
    // Enterprise H2 Database connection coordinates pointing to the build target directory.
    private static final String DB_URL = "jdbc:h2:file:./target/db/ai_qa_framework;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    /**
     * Constructs the database persister and initializes the core schema required for Phase 1.
     */
    public AiDatabasePersister() {
        initializeSchema();
    }

    /**
     * Provisions the relational database tables if they do not already exist.
     * Follows clean architecture by ensuring the database state is ready upon instantiation.
     * 
     * @throws AiExtensionException if the schema creation encounters a SQL constraint.
     */
    private void initializeSchema() {
        String createPlanTable = "CREATE TABLE IF NOT EXISTS test_plans ("
                + "plan_id VARCHAR(255) PRIMARY KEY, "
                + "requirement TEXT, "
                + "ai_context TEXT, "
                + "status VARCHAR(50))";

        String createScenarioTable = "CREATE TABLE IF NOT EXISTS test_scenarios ("
                + "scenario_id VARCHAR(255) PRIMARY KEY, "
                + "plan_id VARCHAR(255), "
                + "description TEXT)";

        String createTestCaseTable = "CREATE TABLE IF NOT EXISTS test_cases ("
                + "case_id VARCHAR(255) PRIMARY KEY, "
                + "scenario_id VARCHAR(255), "
                + "title TEXT, "
                + "steps TEXT)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
             
            statement.execute(createPlanTable);
            statement.execute(createScenarioTable);
            statement.execute(createTestCaseTable);
            LOGGER.info("H2 Database embedded schema initialized successfully.");
            
        } catch (SQLException sqlException) {
            LOGGER.error("Critical failure during H2 Database schema initialization", sqlException);
            throw new AiExtensionException("Failed to provision the internal H2 database schema", sqlException);
        }
    }

    /**
     * Legacy alias required by existing framework execution tests (e.g., AiAssistantTest).
     * Routes the persistence call to the modern enterprise flow to prevent duplicate logic.
     *
     * @param testPlan The TestPlan to be persisted.
     */
    public void persistTestPlan(TestPlan testPlan) {
        LOGGER.info("Routing legacy persistTestPlan execution to the modern saveTestPlan pipeline.");
        this.saveTestPlan(testPlan);
    }

    /**
     * Persists the core TestPlan entity. Uses MERGE to handle both inserts and updates safely.
     *
     * @param testPlan The foundational TestPlan containing AI context and raw requirements.
     */
    public void saveTestPlan(TestPlan testPlan) {
        if (testPlan == null) {
            LOGGER.warn("Attempted to save a null TestPlan. Database operation bypassed.");
            return;
        }

        String sql = "MERGE INTO test_plans (plan_id, requirement, ai_context, status) KEY(plan_id) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            preparedStatement.setString(1, testPlan.getPlanId());
            preparedStatement.setString(2, testPlan.getOriginalRequirement());
            preparedStatement.setString(3, testPlan.getAiContext());
            preparedStatement.setString(4, testPlan.getPlanStatus());
            
            preparedStatement.executeUpdate();
            LOGGER.info("Successfully persisted TestPlan ID: {}", testPlan.getPlanId());
            
        } catch (SQLException sqlException) {
            LOGGER.error("Failed to persist TestPlan to H2 database.", sqlException);
            throw new AiExtensionException("Database transaction failed while saving TestPlan", sqlException);
        }
    }

    /**
     * Persists a collection of AI-generated Test Scenarios using JDBC batch processing.
     *
     * @param scenarios The list of test scenarios extracted from the test plan.
     */
    public void saveTestScenarios(List<TestScenario> scenarios) {
        if (scenarios == null || scenarios.isEmpty()) {
            LOGGER.warn("Attempted to save an empty scenario list. Database operation bypassed.");
            return;
        }

        String sql = "MERGE INTO test_scenarios (scenario_id, plan_id, description) KEY(scenario_id) VALUES (?, ?, ?)";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            connection.setAutoCommit(false);
            
            for (TestScenario scenario : scenarios) {
                preparedStatement.setString(1, scenario.getScenarioId());
                preparedStatement.setString(2, scenario.getPlanId());
                preparedStatement.setString(3, scenario.getScenarioDescription());
                preparedStatement.addBatch();
            }
            
            preparedStatement.executeBatch();
            connection.commit();
            LOGGER.info("Successfully persisted {} Test Scenarios in batch mode.", scenarios.size());
            
        } catch (SQLException sqlException) {
            LOGGER.error("Failed to persist Test Scenarios to H2 database.", sqlException);
            throw new AiExtensionException("Database batch transaction failed while saving Test Scenarios", sqlException);
        }
    }

    /**
     * Persists a collection of executable Test Cases using JDBC batch processing.
     *
     * @param testCases The list of concrete test cases mapped to the scenarios.
     */
    public void saveTestCases(List<TestCase> testCases) {
        if (testCases == null || testCases.isEmpty()) {
            LOGGER.warn("Attempted to save an empty test case list. Database operation bypassed.");
            return;
        }

        String sql = "MERGE INTO test_cases (case_id, scenario_id, title, steps) KEY(case_id) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            connection.setAutoCommit(false);
            
            for (TestCase testCase : testCases) {
                preparedStatement.setString(1, testCase.getTestCaseId());
                preparedStatement.setString(2, testCase.getScenarioId());
                preparedStatement.setString(3, testCase.getTestCaseTitle());
                preparedStatement.setString(4, testCase.getTestSteps());
                preparedStatement.addBatch();
            }
            
            preparedStatement.executeBatch();
            connection.commit();
            LOGGER.info("Successfully persisted {} Test Cases in batch mode.", testCases.size());
            
        } catch (SQLException sqlException) {
            LOGGER.error("Failed to persist Test Cases to H2 database.", sqlException);
            throw new AiExtensionException("Database batch transaction failed while saving Test Cases", sqlException);
        }
    }
}