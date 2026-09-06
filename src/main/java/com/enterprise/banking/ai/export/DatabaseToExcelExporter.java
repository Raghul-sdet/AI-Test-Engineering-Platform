package com.enterprise.banking.ai.export;

import com.enterprise.banking.ai.exception.AiExtensionException;
import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.model.TestScenario;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Generates an Enterprise Traceability Matrix in Excel format using Apache POI.
 * Upgraded to an IT Corporate standard template with dashboards, conditional formatting,
 * and high-readability styles.
 */
public class DatabaseToExcelExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseToExcelExporter.class);

    private CellStyle headerStyle;
    private CellStyle defaultDataStyle;
    private CellStyle altDataStyle;
    private CellStyle highRiskStyle;
    private CellStyle mediumRiskStyle;
    private CellStyle lowRiskStyle;
    private CellStyle metadataLabelStyle;
    private CellStyle metadataValueStyle;

    public DatabaseToExcelExporter() {}

    public File exportToExcel(TestPlan testPlan, String exportFilePath) {
        if (testPlan == null || exportFilePath == null || exportFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("TestPlan and export path cannot be null");
        }

        LOGGER.info("Initiating Enterprise Traceability Excel generation for Plan ID: {}", testPlan.getPlanId());

        try {
            ensureDirectoryExists(exportFilePath);
            return generateWorkbook(testPlan, exportFilePath);
        } catch (IOException ioException) {
            LOGGER.error("Fatal IO exception during Excel generation", ioException);
            throw new AiExtensionException("Failed to generate Excel Traceability Report", ioException);
        }
    }

    private void ensureDirectoryExists(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Path parentDir = path.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }
    }

    private void initializeStyles(Workbook workbook) {
        // Headers (Dark Blue, White Text)
        headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        applyBorders(headerStyle);

        // Data Row 1 (White)
        defaultDataStyle = createBaseDataStyle(workbook, IndexedColors.WHITE.getIndex());
        
        // Data Row 2 (Light Grey)
        altDataStyle = createBaseDataStyle(workbook, IndexedColors.GREY_25_PERCENT.getIndex());
        
        // Conditional Risk Styles
        highRiskStyle = createConditionalStyle(workbook, IndexedColors.ROSE.getIndex());
        mediumRiskStyle = createConditionalStyle(workbook, IndexedColors.LIGHT_YELLOW.getIndex());
        lowRiskStyle = createConditionalStyle(workbook, IndexedColors.LIGHT_GREEN.getIndex());

        // Metadata Label (Bold, Light Grey)
        metadataLabelStyle = workbook.createCellStyle();
        Font metaFont = workbook.createFont();
        metaFont.setBold(true);
        metadataLabelStyle.setFont(metaFont);
        metadataLabelStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        metadataLabelStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        applyBorders(metadataLabelStyle);

        // Metadata Value
        metadataValueStyle = workbook.createCellStyle();
        applyBorders(metadataValueStyle);
    }

    private CellStyle createBaseDataStyle(Workbook workbook, short bgIndex) {
        CellStyle style = workbook.createCellStyle();
        applyBorders(style);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        if (bgIndex != IndexedColors.WHITE.getIndex()) {
            style.setFillForegroundColor(bgIndex);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        return style;
    }

    private CellStyle createConditionalStyle(Workbook workbook, short bgIndex) {
        CellStyle style = workbook.createCellStyle();
        applyBorders(style);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(bgIndex);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private void applyBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    }

    private File generateWorkbook(TestPlan testPlan, String exportFilePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            initializeStyles(workbook);
            Sheet sheet = workbook.createSheet("Test Design Report");
            
            // Build Dashboard Header
            int nextRow = buildMetadataDashboard(sheet, testPlan);
            
            // Freeze panes (lock dashboard and headers)
            sheet.createFreezePane(0, nextRow + 1);
            
            // Headers
            buildHeaderRow(sheet, nextRow);
            
            // Data
            populateDataRows(sheet, testPlan, nextRow + 1);
            
            // Auto-filter on the header row
            sheet.setAutoFilter(new CellRangeAddress(nextRow, nextRow, 0, 8));
            
            // Auto-size columns (max width 255*256 to avoid extremely wide columns on steps)
            for (int i = 0; i < 9; i++) {
                sheet.autoSizeColumn(i);
                if (sheet.getColumnWidth(i) > 100 * 256) {
                    sheet.setColumnWidth(i, 100 * 256); // Set max width limit for Execution Steps
                }
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(exportFilePath)) {
                workbook.write(fileOutputStream);
            }
            
            return new File(exportFilePath);
        }
    }

    private int buildMetadataDashboard(Sheet sheet, TestPlan testPlan) {
        int totalScenarios = testPlan.scenarios() != null ? testPlan.scenarios().size() : 0;
        int totalTestCases = 0;
        if (testPlan.scenarios() != null) {
            for (TestScenario s : testPlan.scenarios()) {
                if (s.getTestCases() != null) totalTestCases += s.getTestCases().size();
            }
        }

        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Row 0: Title
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Enterprise AI QA Automation Report");
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short)16);
        titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
        titleStyle.setFont(titleFont);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        // Rows 2-5: Metadata
        createMetaRow(sheet, 2, "Test Plan ID:", testPlan.getPlanId());
        createMetaRow(sheet, 3, "Feature / Requirement:", testPlan.featureName());
        createMetaRow(sheet, 4, "Generation Date:", dateStr);
        createMetaRow(sheet, 5, "Total Scenarios:", String.valueOf(totalScenarios));
        createMetaRow(sheet, 6, "Total Test Cases:", String.valueOf(totalTestCases));
        
        return 8; // Row index where headers will start
    }
    
    private void createMetaRow(Sheet sheet, int rowIdx, String label, String value) {
        Row row = sheet.createRow(rowIdx);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(metadataLabelStyle);
        
        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value == null ? "N/A" : value);
        valueCell.setCellStyle(metadataValueStyle);
        
        sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 1, 3));
    }

    private void buildHeaderRow(Sheet sheet, int rowIndex) {
        Row headerRow = sheet.createRow(rowIndex);
        String[] headers = {
            "Requirement ID", "Feature Name", "Scenario ID", "Scenario Title", 
            "Test Case ID", "Test Case Title", "Execution Steps", "Priority", "Severity"
        };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void populateDataRows(Sheet sheet, TestPlan testPlan, int startRow) {
        int rowIndex = startRow;
        
        List<TestScenario> scenarios = testPlan.scenarios();
        if (scenarios == null || scenarios.isEmpty()) return;

        int dataCount = 0;
        for (TestScenario scenario : scenarios) {
            List<TestCase> testCases = scenario.getTestCases();
            if (testCases != null && !testCases.isEmpty()) {
                for (TestCase testCase : testCases) {
                    boolean isAlternate = (dataCount % 2 == 1);
                    Row row = sheet.createRow(rowIndex++);
                    writeRowData(row, testPlan, scenario, testCase, isAlternate);
                    dataCount++;
                }
            } else {
                boolean isAlternate = (dataCount % 2 == 1);
                Row row = sheet.createRow(rowIndex++);
                writeRowData(row, testPlan, scenario, null, isAlternate);
                dataCount++;
            }
        }
    }

    private void writeRowData(Row row, TestPlan testPlan, TestScenario scenario, TestCase testCase, boolean isAlternate) {
        CellStyle style = isAlternate ? altDataStyle : defaultDataStyle;

        createStyledCell(row, 0, testPlan.requirementId(), style);
        createStyledCell(row, 1, testPlan.featureName(), style);
        createStyledCell(row, 2, scenario.id(), style);
        createStyledCell(row, 3, scenario.scenarioTitle(), style);

        if (testCase != null) {
            createStyledCell(row, 4, testCase.testCaseId(), style);
            createStyledCell(row, 5, testCase.testCaseTitle(), style);
            createStyledCell(row, 6, testCase.getTestSteps(), style);
            
            String priority = testCase.priority();
            String severity = testCase.severity();
            
            createStyledCell(row, 7, priority, resolveRiskStyle(priority, isAlternate));
            createStyledCell(row, 8, severity, resolveRiskStyle(severity, isAlternate));
        } else {
            createStyledCell(row, 4, "Pending Generation", style);
            createStyledCell(row, 5, "Pending Generation", style);
            createStyledCell(row, 6, "N/A", style);
            createStyledCell(row, 7, "N/A", style);
            createStyledCell(row, 8, "N/A", style);
        }
    }

    private void createStyledCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value != null ? value : "N/A");
        cell.setCellStyle(style);
    }
    
    private CellStyle resolveRiskStyle(String value, boolean isAlternate) {
        if (value == null) return isAlternate ? altDataStyle : defaultDataStyle;
        String upper = value.trim().toUpperCase();
        
        if (upper.contains("HIGH") || upper.contains("CRITICAL") || upper.contains("P1")) {
            return highRiskStyle;
        } else if (upper.contains("MEDIUM") || upper.contains("MAJOR") || upper.contains("P2")) {
            return mediumRiskStyle;
        } else if (upper.contains("LOW") || upper.contains("MINOR") || upper.contains("P3")) {
            return lowRiskStyle;
        }
        return isAlternate ? altDataStyle : defaultDataStyle;
    }
}