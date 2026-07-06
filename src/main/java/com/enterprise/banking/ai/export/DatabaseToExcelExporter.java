package com.enterprise.banking.ai.export;

import com.enterprise.banking.ai.exception.AiExtensionException;
import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.model.TestScenario;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Generates an Enterprise Traceability Matrix in Excel format using Apache POI.
 * Transforms hierarchical TestPlan data into a flat spreadsheet for stakeholder reporting.
 */
public class DatabaseToExcelExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseToExcelExporter.class);

    /**
     * Default constructor for framework initialization.
     */
    public DatabaseToExcelExporter() {
        // Intentionally left empty for framework instantiation
    }

    /**
     * Exports the provided TestPlan into an Excel format and saves it to the specified path.
     *
     * @param testPlan       The core TestPlan entity containing scenarios and test cases.
     * @param exportFilePath The absolute or relative physical path where the Excel file should be saved.
     * @return The generated java.io.File object representing the Excel report.
     * @throws AiExtensionException if file I/O operations fail during generation.
     */
    public File exportToExcel(TestPlan testPlan, String exportFilePath) {
        if (testPlan == null) {
            LOGGER.error("TestPlan provided for Excel export is null.");
            throw new IllegalArgumentException("TestPlan cannot be null");
        }
        if (exportFilePath == null || exportFilePath.trim().isEmpty()) {
            LOGGER.error("Export file path provided is null or empty.");
            throw new IllegalArgumentException("Export file path cannot be null or empty");
        }

        LOGGER.info("Initiating Enterprise Traceability Excel generation for Plan ID: {}", testPlan.getPlanId());

        try {
            ensureDirectoryExists(exportFilePath);
            return generateWorkbook(testPlan, exportFilePath);
        } catch (IOException ioException) {
            LOGGER.error("Encountered fatal IO exception during Excel generation: {}", ioException.getMessage(), ioException);
            throw new AiExtensionException("Failed to generate Excel Traceability Report", ioException);
        }
    }

    /**
     * Ensures that the parent directories for the target file path exist.
     *
     * @param filePath The complete file path.
     * @throws IOException if directories cannot be created.
     */
    private void ensureDirectoryExists(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Path parentDir = path.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
            LOGGER.debug("Created parent directories for Excel export: {}", parentDir.toAbsolutePath());
        }
    }

    /**
     * Handles the Apache POI workbook creation and data population logic.
     *
     * @param testPlan       The TestPlan entity.
     * @param exportFilePath The target file path.
     * @return The saved File object.
     * @throws IOException if writing to the file stream fails.
     */
    private File generateWorkbook(TestPlan testPlan, String exportFilePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("AI Traceability Matrix");
            
            buildHeaderRow(workbook, sheet);
            populateDataRows(sheet, testPlan);
            
            // Auto-size columns for better readability
            for (int i = 0; i < 9; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(exportFilePath)) {
                workbook.write(fileOutputStream);
            }
            
            LOGGER.info("Successfully exported Traceability Matrix to: {}", exportFilePath);
            return new File(exportFilePath);
        }
    }

    /**
     * Constructs and formats the header row of the spreadsheet.
     *
     * @param workbook The active POI workbook.
     * @param sheet    The active sheet.
     */
    private void buildHeaderRow(Workbook workbook, Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "Requirement ID", "Feature Name", "Scenario ID", "Scenario Title", 
            "Test Case ID", "Test Case Title", "Execution Steps", "Priority", "Severity"
        };
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * Iterates through the hierarchical TestPlan to populate spreadsheet rows.
     *
     * @param sheet    The active POI sheet.
     * @param testPlan The root TestPlan entity.
     */
    private void populateDataRows(Sheet sheet, TestPlan testPlan) {
        int rowIndex = 1;
        
        List<TestScenario> scenarios = testPlan.scenarios();
        if (scenarios == null || scenarios.isEmpty()) {
            LOGGER.warn("No scenarios found in TestPlan ID: {}. Report will be largely empty.", testPlan.getPlanId());
            return;
        }

        for (TestScenario scenario : scenarios) {
            List<TestCase> testCases = scenario.getTestCases();
            
            if (testCases != null && !testCases.isEmpty()) {
                for (TestCase testCase : testCases) {
                    Row row = sheet.createRow(rowIndex++);
                    writeRowData(row, testPlan, scenario, testCase);
                }
            } else {
                // Write a row even if no concrete test cases exist for the scenario yet
                Row row = sheet.createRow(rowIndex++);
                writeRowData(row, testPlan, scenario, null);
            }
        }
    }

    /**
     * Writes individual cell data for a specific mapping.
     *
     * @param row      The active POI row.
     * @param testPlan The root plan context.
     * @param scenario The specific scenario context.
     * @param testCase The specific test case context (nullable).
     */
    private void writeRowData(Row row, TestPlan testPlan, TestScenario scenario, TestCase testCase) {
        row.createCell(0).setCellValue(testPlan.requirementId() != null ? testPlan.requirementId() : "N/A");
        row.createCell(1).setCellValue(testPlan.featureName() != null ? testPlan.featureName() : "N/A");
        
        row.createCell(2).setCellValue(scenario.id() != null ? scenario.id() : "N/A");
        row.createCell(3).setCellValue(scenario.scenarioTitle() != null ? scenario.scenarioTitle() : "N/A");
        
        if (testCase != null) {
            row.createCell(4).setCellValue(testCase.testCaseId() != null ? testCase.testCaseId() : "N/A");
            row.createCell(5).setCellValue(testCase.testCaseTitle() != null ? testCase.testCaseTitle() : "N/A");
            row.createCell(6).setCellValue(testCase.getTestSteps() != null ? testCase.getTestSteps() : "N/A");
            row.createCell(7).setCellValue(testCase.priority() != null ? testCase.priority() : "N/A");
            row.createCell(8).setCellValue(testCase.severity() != null ? testCase.severity() : "N/A");
        } else {
            row.createCell(4).setCellValue("Pending Generation");
            row.createCell(5).setCellValue("Pending Generation");
            row.createCell(6).setCellValue("N/A");
            row.createCell(7).setCellValue("N/A");
            row.createCell(8).setCellValue("N/A");
        }
    }
}