package com.enterprise.banking.ai.export;

import com.enterprise.banking.ai.model.TestStatistics;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Map;

/**
 * Handles the specialized Apache POI logic for drawing the Statistics Dashboard sheet.
 */
public class DashboardGenerator {

    public void generateDashboardSheet(Workbook workbook, TestStatistics stats) {
        Sheet sheet = workbook.createSheet("Statistics Dashboard");
        sheet.setDisplayGridlines(false);

        // Styles
        CellStyle titleStyle = createTitleStyle(workbook);
        CellStyle labelStyle = createLabelStyle(workbook);
        CellStyle valueStyle = createValueStyle(workbook);

        // Title
        Row titleRow = sheet.createRow(1);
        Cell titleCell = titleRow.createCell(1);
        titleCell.setCellValue("ENTERPRISE AI TEST DESIGN DASHBOARD");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 5));

        // High-Level Metrics
        int rowIdx = 4;
        createMetricRow(sheet, rowIdx++, "Total Requirements Evaluated", String.valueOf(stats.totalRequirements()), labelStyle, valueStyle);
        createMetricRow(sheet, rowIdx++, "Total Scenarios Generated", String.valueOf(stats.totalScenarios()), labelStyle, valueStyle);
        createMetricRow(sheet, rowIdx++, "Total Test Cases Generated", String.valueOf(stats.totalTestCases()), labelStyle, valueStyle);
        createMetricRow(sheet, rowIdx++, "Total Test Steps Executable", String.valueOf(stats.totalTestSteps()), labelStyle, valueStyle);

        rowIdx += 2;

        // Coverage Distribution
        rowIdx = createDistributionTable(sheet, rowIdx, "Scenario Coverage by Category", stats.categoryDistribution(), labelStyle, valueStyle);
        rowIdx += 2;
        
        // Priority Distribution
        rowIdx = createDistributionTable(sheet, rowIdx, "Risk Analysis: Priority Distribution", stats.priorityDistribution(), labelStyle, valueStyle);
        rowIdx += 2;

        // Severity Distribution
        createDistributionTable(sheet, rowIdx, "Risk Analysis: Severity Distribution", stats.severityDistribution(), labelStyle, valueStyle);

        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 5000);
    }

    private void createMetricRow(Sheet sheet, int rowNum, String label, String value, CellStyle labelStyle, CellStyle valueStyle) {
        Row row = sheet.createRow(rowNum);
        Cell cellLabel = row.createCell(1);
        cellLabel.setCellValue(label);
        cellLabel.setCellStyle(labelStyle);

        Cell cellValue = row.createCell(2);
        cellValue.setCellValue(value);
        cellValue.setCellStyle(valueStyle);
    }

    private int createDistributionTable(Sheet sheet, int startRow, String tableTitle, Map<String, Integer> data, CellStyle labelStyle, CellStyle valueStyle) {
        Row titleRow = sheet.createRow(startRow++);
        Cell titleCell = titleRow.createCell(1);
        titleCell.setCellValue(tableTitle);
        titleCell.setCellStyle(labelStyle);

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            createMetricRow(sheet, startRow++, entry.getKey(), String.valueOf(entry.getValue()), labelStyle, valueStyle);
        }
        return startRow;
    }

    private CellStyle createTitleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createLabelStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createValueStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}