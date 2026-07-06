package com.enterprise.banking.ai.export;

import com.enterprise.banking.ai.exception.AiExtensionException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Handles the creation, formatting, and file generation of Excel workbooks
 * using Apache POI. Ensures enterprise-grade formatting standards.
 */
public class ExcelReportGenerator {

    private Workbook workbook;
    
    // Core Styles
    private CellStyle headerStyle;
    private CellStyle defaultDataStyle;
    private CellStyle altDataStyle;
    
    // Conditional Styles
    private CellStyle highRiskStyle;
    private CellStyle mediumRiskStyle;
    private CellStyle lowRiskStyle;

    public ExcelReportGenerator() {
        this.workbook = new XSSFWorkbook();
        initializeStyles();
    }

    /**
     * Injects an external workbook and re-initializes styles.
     *
     * @param workbook The shared workbook instance.
     */
    public void setWorkbook(Workbook workbook) {
        if (workbook == null) {
            throw new IllegalArgumentException("Workbook cannot be null");
        }
        this.workbook = workbook;
        initializeStyles();
    }

    /**
     * Centralized style initialization to ensure styles are only created once per workbook,
     * preventing "Too many formatting records" exceptions.
     */
    private void initializeStyles() {
        this.headerStyle = createHeaderStyle();
        this.defaultDataStyle = createBaseDataStyle(IndexedColors.WHITE.getIndex());
        this.altDataStyle = createBaseDataStyle(IndexedColors.GREY_25_PERCENT.getIndex());
        
        this.highRiskStyle = createConditionalStyle(IndexedColors.ROSE.getIndex());
        this.mediumRiskStyle = createConditionalStyle(IndexedColors.LIGHT_YELLOW.getIndex());
        this.lowRiskStyle = createConditionalStyle(IndexedColors.LIGHT_GREEN.getIndex());
    }

    public void createSheetWithData(String sheetName, String[] headers, List<Map<String, String>> data) {
        if (sheetName == null || sheetName.isBlank()) {
            sheetName = "Sheet" + (workbook.getNumberOfSheets() + 1);
        }
        
        // Prevent duplicate sheet name exception
        String finalSheetName = sheetName;
        if (workbook.getSheet(finalSheetName) != null) {
            finalSheetName = sheetName + "_" + System.currentTimeMillis();
        }

        Sheet sheet = workbook.createSheet(finalSheetName);
        
        // Enterprise features: Freeze Header
        sheet.createFreezePane(0, 1);

        if (headers == null || headers.length == 0) {
            return;
        }

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i] != null ? headers[i] : "");
            cell.setCellStyle(headerStyle);
        }

        // Enterprise feature: Auto Filter on headers
        sheet.setAutoFilter(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, headers.length - 1));

        if (data == null || data.isEmpty()) {
            return;
        }

        // Populate Data Rows with Alternate Shading and Conditional Formatting
        int rowNum = 1;
        for (Map<String, String> rowData : data) {
            if (rowData == null) continue;
            
            Row row = sheet.createRow(rowNum);
            boolean isAlternate = (rowNum % 2 == 0);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = row.createCell(i);
                String value = rowData.getOrDefault(headers[i], "");
                
                cell.setCellValue(value != null ? value : "");
                cell.setCellStyle(resolveStyle(value, isAlternate));
            }
            rowNum++;
        }

        // Auto-size columns for professional appearance (with reasonable max width limit)
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            // Limit max width to avoid extremely wide columns on long text
            if (sheet.getColumnWidth(i) > 255 * 256) {
                sheet.setColumnWidth(i, 255 * 256);
            }
        }
    }

    public void writeToFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new AiExtensionException("Failed to write Excel workbook to path: " + filePath, e);
        } finally {
            try {
                workbook.close();
            } catch (IOException ignored) {
            }
        }
    }

    private CellStyle resolveStyle(String value, boolean isAlternateRow) {
        if (value == null || value.isBlank()) {
            return isAlternateRow ? altDataStyle : defaultDataStyle;
        }
        
        String upperValue = value.trim().toUpperCase();
        
        // Critical / High Risk conditions
        if (upperValue.equals("HIGH") || upperValue.equals("CRITICAL") || 
            upperValue.equals("ELEVATED RISK") || upperValue.equals("NO") || 
            upperValue.equals("FAIL") || upperValue.equals("REJECTED")) {
            return highRiskStyle;
        }
        // Medium Risk conditions
        else if (upperValue.equals("MEDIUM") || upperValue.equals("MAJOR")) {
            return mediumRiskStyle;
        }
        // Low Risk / Positive conditions
        else if (upperValue.equals("LOW") || upperValue.equals("MINOR") || 
                 upperValue.equals("NORMAL") || upperValue.equals("YES") || 
                 upperValue.equals("COVERED") || upperValue.equals("PASS") ||
                 upperValue.equals("SUCCESS")) {
            return lowRiskStyle;
        }
        
        return isAlternateRow ? altDataStyle : defaultDataStyle;
    }

    private CellStyle createHeaderStyle() {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        
        // Deep Blue Header for Enterprise Look
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        applyBorders(style);
        return style;
    }

    private CellStyle createBaseDataStyle(short bgIndex) {
        CellStyle style = workbook.createCellStyle();
        applyBorders(style);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true); // Allow text wrapping for long step descriptions
        
        if (bgIndex != IndexedColors.WHITE.getIndex()) {
            style.setFillForegroundColor(bgIndex);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        return style;
    }
    
    private CellStyle createConditionalStyle(short bgIndex) {
        CellStyle style = workbook.createCellStyle();
        applyBorders(style);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(bgIndex);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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
}