package com.enterprise.banking.ai.export;

import com.enterprise.banking.ai.model.TestResultRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExecutionResultExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionResultExporter.class);

    private CellStyle headerStyle;
    private CellStyle dataStylePass;
    private CellStyle dataStyleFail;
    private CellStyle dataStyleSkip;
    private CellStyle defaultDataStyle;
    private CellStyle sectionHeaderStyle;

    public void appendExecutionResultsToExcel(List<TestResultRecord> results, String exportFilePath) {
        if (exportFilePath == null || exportFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Export path cannot be null");
        }

        LOGGER.info("Appending Execution Results to Excel: {}", exportFilePath);

        try {
            ensureDirectoryExists(exportFilePath);
            File file = new File(exportFilePath);
            Workbook workbook;

            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fis);
                }
            } else {
                workbook = new XSSFWorkbook();
            }

            initializeStyles(workbook);

            int totalScenarios = extractCount(workbook, "Total Scenarios:");
            int totalTestCases = extractCount(workbook, "Total Test Cases:");

            // Remove all result sheets before rebuilding — also removes the legacy
            // "AI Pipeline Test Results" sheet from any older workbook on disk.
            String[] sheetNames = {
                "Statistics Dashboard", "UI Test Results", "API Test Results",
                "Database Test Results", "Hybrid E2E Test Results",
                "AI Pipeline Test Results", "AI Component Test Results",
                "Performance Test Results"
            };
            for (String sheetName : sheetNames) {
                int index = workbook.getSheetIndex(sheetName);
                if (index != -1) {
                    workbook.removeSheetAt(index);
                }
            }

            // Route AI pipeline results into the AI component bucket so the final workbook
            // has exactly 7 sheets — no separate "AI Pipeline Test Results" sheet.
            List<TestResultRecord> apiTests = results.stream().filter(this::isApiTest).collect(Collectors.toList());
            List<TestResultRecord> dbTests = results.stream().filter(this::isDbTest).collect(Collectors.toList());
            List<TestResultRecord> hybridTests = results.stream().filter(this::isHybridTest).collect(Collectors.toList());
            List<TestResultRecord> uiTests = results.stream().filter(this::isUiTest).collect(Collectors.toList());
            // AI Component = everything that is not UI / API / DB / Hybrid (includes pipeline tests)
            List<TestResultRecord> aiTests = results.stream()
                    .filter(r -> !isApiTest(r) && !isDbTest(r) && !isHybridTest(r) && !isUiTest(r))
                    .collect(Collectors.toList());

            List<JMeterSummary> jmeterResults = parseJMeterResults("target/jmeter/results/api-performance.csv");

            Sheet statsSheet = workbook.createSheet("Statistics Dashboard");
            buildStatisticsDashboard(statsSheet, results, uiTests, apiTests, dbTests, hybridTests, aiTests, jmeterResults, totalScenarios, totalTestCases);

            buildResultSheet(workbook.createSheet("UI Test Results"), uiTests);
            buildResultSheet(workbook.createSheet("API Test Results"), apiTests);
            buildResultSheet(workbook.createSheet("Database Test Results"), dbTests);
            buildResultSheet(workbook.createSheet("Hybrid E2E Test Results"), hybridTests);
            buildResultSheet(workbook.createSheet("AI Component Test Results"), aiTests);
            buildJMeterResultSheet(workbook.createSheet("Performance Test Results"), jmeterResults);

            // Enforce the mandatory 8-sheet order in the workbook.
            // "Test Design Report" is written by DatabaseToExcelExporter before this method runs;
            // if it exists, slot it at position 1; otherwise the remaining 6 sheets shift left.
            String[] requiredOrder = {
                "Statistics Dashboard",
                "Test Design Report",
                "UI Test Results",
                "API Test Results",
                "Database Test Results",
                "Hybrid E2E Test Results",
                "AI Component Test Results",
                "Performance Test Results"
            };
            int targetPos = 0;
            for (String name : requiredOrder) {
                int idx = workbook.getSheetIndex(name);
                if (idx != -1) {
                    workbook.setSheetOrder(name, targetPos++);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
            workbook.close();

        } catch (IOException ioException) {
            LOGGER.error("Fatal IO exception during execution export", ioException);
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
        headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        applyBorders(headerStyle);

        defaultDataStyle = workbook.createCellStyle();
        applyBorders(defaultDataStyle);

        dataStylePass = workbook.createCellStyle();
        dataStylePass.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        dataStylePass.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        applyBorders(dataStylePass);

        dataStyleFail = workbook.createCellStyle();
        dataStyleFail.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        dataStyleFail.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        applyBorders(dataStyleFail);

        dataStyleSkip = workbook.createCellStyle();
        dataStyleSkip.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        dataStyleSkip.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        applyBorders(dataStyleSkip);
        
        sectionHeaderStyle = workbook.createCellStyle();
        Font sectionFont = workbook.createFont();
        sectionFont.setBold(true);
        sectionHeaderStyle.setFont(sectionFont);
        sectionHeaderStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        sectionHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        applyBorders(sectionHeaderStyle);
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

    private int extractCount(Workbook workbook, String labelPrefix) {
        Sheet sheet = workbook.getSheet("Test Design Report");
        if (sheet != null) {
            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().startsWith(labelPrefix)) {
                        Cell valueCell = row.getCell(1);
                        if (valueCell != null) {
                            if (valueCell.getCellType() == CellType.NUMERIC) {
                                return (int) valueCell.getNumericCellValue();
                            } else if (valueCell.getCellType() == CellType.STRING) {
                                try {
                                    return Integer.parseInt(valueCell.getStringCellValue().trim());
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    private boolean isApiTest(TestResultRecord record) {
        return record.getClassName().startsWith("com.enterprise.banking.api.tests.");
    }

    private boolean isDbTest(TestResultRecord record) {
        return record.getClassName().equals("com.enterprise.banking.tests.DatabaseValidationTest");
    }

    private boolean isHybridTest(TestResultRecord record) {
        return record.getClassName().startsWith("com.enterprise.banking.hybrid.tests.");
    }

    
    private boolean isUiTest(TestResultRecord record) {
        String name = record.getClassName();
        if (name.startsWith("com.enterprise.banking.tests.")) {
            return name.endsWith(".RegistrationTest") 
                || name.endsWith(".AccountOverviewTest") 
                || name.endsWith(".TransferFundsTest") 
                || name.endsWith(".FindTransactionsTest") 
                || name.endsWith(".OpenAccountTest");
        }
        return false;
    }


    private void buildStatisticsDashboard(Sheet sheet, List<TestResultRecord> all, List<TestResultRecord> ui, List<TestResultRecord> api, List<TestResultRecord> db, List<TestResultRecord> hybrid, List<TestResultRecord> ai, List<JMeterSummary> jmeterResults, int aiScenarios, int aiTestCases) {
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Test Execution Statistics Dashboard");
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short)16);
        titleFont.setColor(IndexedColors.WHITE.getIndex());
        titleStyle.setFont(titleFont);
        titleStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleCell.setCellStyle(titleStyle);
        
        Cell titleCell1 = titleRow.createCell(1);
        titleCell1.setCellStyle(titleStyle);
        Cell titleCell2 = titleRow.createCell(2);
        titleCell2.setCellStyle(titleStyle);
        
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

        sheet.createFreezePane(0, 1);

        int row = 2;
        row = writeSectionHeader(sheet, row, "AI Generation Stats");
        row = writeStatRow(sheet, row, "AI Generated Scenarios", aiScenarios);
        row = writeStatRow(sheet, row, "AI Generated Test Cases", aiTestCases);
        
        row++;
        
        row = writeSectionHeader(sheet, row, "Overall Execution Stats");
        long passCount = all.stream().filter(r -> "PASS".equals(r.getStatus())).count();
        long failCount = all.stream().filter(r -> "FAIL".equals(r.getStatus())).count();
        long skipCount = all.stream().filter(r -> "SKIP".equals(r.getStatus())).count();
        
        row = writeStatRow(sheet, row, "Total Tests Run", all.size());
        row = writeStatRow(sheet, row, "Total Passed", passCount);
        row = writeStatRow(sheet, row, "Total Failed", failCount);
        row = writeStatRow(sheet, row, "Total Skipped", skipCount);

        row++;

        row = writeSectionHeader(sheet, row, "Execution By Category");
        row = writeStatRow(sheet, row, "UI Tests", ui.size());
        row = writeStatRow(sheet, row, "API Tests", api.size());
        row = writeStatRow(sheet, row, "Database Tests", db.size());
        row = writeStatRow(sheet, row, "Hybrid Tests", hybrid.size());
        // AI Component count includes former pipeline tests (no separate sheet)
        row = writeStatRow(sheet, row, "AI Component Tests", ai.size());
        int jmeterCount = jmeterResults.stream().mapToInt(j -> j.count).sum();
        row = writeStatRow(sheet, row, "Performance Tests", jmeterCount);
        
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        
        if (sheet instanceof XSSFSheet) {
            XSSFSheet xssfSheet = (XSSFSheet) sheet;
            XSSFDrawing drawing = xssfSheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 4, 2, 12, 17);
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("Test Distribution");
            chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromArray(new String[] {"UI", "API", "DB", "Hybrid", "AI", "Perf"});
            XDDFNumericalDataSource<Integer> values = XDDFDataSourcesFactory.fromArray(new Integer[] {ui.size(), api.size(), db.size(), hybrid.size(), ai.size(), jmeterCount});

            XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
            XDDFChartData.Series series = data.addSeries(categories, values);
            series.setTitle("Test Count", null);
            chart.plot(data);
        }
    }
    
    private int writeSectionHeader(Sheet sheet, int rowIdx, String headerText) {
        Row row = sheet.createRow(rowIdx);
        Cell cell = row.createCell(0);
        cell.setCellValue(headerText);
        cell.setCellStyle(sectionHeaderStyle);
        
        Cell cell2 = row.createCell(1);
        cell2.setCellStyle(sectionHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 1));
        return rowIdx + 1;
    }

    private int writeStatRow(Sheet sheet, int rowIdx, String label, long value) {
        Row row = sheet.createRow(rowIdx);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(label);
        cell1.setCellStyle(defaultDataStyle);
        
        Cell cell2 = row.createCell(1);
        cell2.setCellValue((double) value);
        cell2.setCellStyle(defaultDataStyle);
        return rowIdx + 1;
    }

    private void buildResultSheet(Sheet sheet, List<TestResultRecord> records) {
        sheet.createFreezePane(0, 1);
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Test Name", "Class", "Status", "Duration (ms)", "Failure Reason", "Timestamp"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 1;
        for (TestResultRecord record : records) {
            Row row = sheet.createRow(rowIndex++);
            
            Cell nameCell = row.createCell(0);
            nameCell.setCellValue(record.getTestName());
            nameCell.setCellStyle(defaultDataStyle);
            
            Cell classCell = row.createCell(1);
            classCell.setCellValue(record.getClassName());
            classCell.setCellStyle(defaultDataStyle);
            
            Cell statusCell = row.createCell(2);
            statusCell.setCellValue(record.getStatus());
            if ("PASS".equals(record.getStatus())) statusCell.setCellStyle(dataStylePass);
            else if ("FAIL".equals(record.getStatus())) statusCell.setCellStyle(dataStyleFail);
            else if ("SKIP".equals(record.getStatus())) statusCell.setCellStyle(dataStyleSkip);
            else statusCell.setCellStyle(defaultDataStyle);
            
            Cell durationCell = row.createCell(3);
            durationCell.setCellValue(record.getDurationMs());
            durationCell.setCellStyle(defaultDataStyle);
            
            Cell failCell = row.createCell(4);
            failCell.setCellValue(record.getFailureReason() != null ? record.getFailureReason() : "");
            failCell.setCellStyle(defaultDataStyle);
            
            Cell timeCell = row.createCell(5);
            timeCell.setCellValue(record.getTimestamp());
            timeCell.setCellStyle(defaultDataStyle);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            if (sheet.getColumnWidth(i) > 255 * 256) {
                sheet.setColumnWidth(i, 255 * 256);
            }
        }
    }

    private static class JMeterSummary {
        String endpoint;
        long totalElapsed;
        long maxElapsed;
        int count;
        int errors;

        JMeterSummary(String endpoint) {
            this.endpoint = endpoint;
        }
    }

    private List<JMeterSummary> parseJMeterResults(String jtlFilePath) {
        Map<String, JMeterSummary> summaries = new HashMap<>();
        try {
            Path path = Paths.get(jtlFilePath);
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }
            List<String> lines = Files.readAllLines(path);
            if (lines.size() <= 1) return new ArrayList<>(); // Only header or empty
            
            String[] headers = lines.get(0).split(",");
            int labelIdx = -1, elapsedIdx = -1, successIdx = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equalsIgnoreCase("label")) labelIdx = i;
                else if (headers[i].equalsIgnoreCase("elapsed")) elapsedIdx = i;
                else if (headers[i].equalsIgnoreCase("success")) successIdx = i;
            }
            
            if (labelIdx == -1 || elapsedIdx == -1 || successIdx == -1) {
                LOGGER.warn("JMeter JTL file missing expected columns");
                return new ArrayList<>();
            }
            
            for (int i = 1; i < lines.size(); i++) {
                String[] cols = lines.get(i).split(",");
                if (cols.length <= Math.max(labelIdx, Math.max(elapsedIdx, successIdx))) continue;
                
                String label = cols[labelIdx];
                long elapsed = Long.parseLong(cols[elapsedIdx]);
                boolean success = Boolean.parseBoolean(cols[successIdx]);
                
                JMeterSummary summary = summaries.computeIfAbsent(label, JMeterSummary::new);
                summary.count++;
                summary.totalElapsed += elapsed;
                if (elapsed > summary.maxElapsed) summary.maxElapsed = elapsed;
                if (!success) summary.errors++;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse JMeter results", e);
        }
        return new ArrayList<>(summaries.values());
    }

    private void buildJMeterResultSheet(Sheet sheet, List<JMeterSummary> records) {
        sheet.createFreezePane(0, 2);
        
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Note: These results reflect the framework's load-handling capability against a local WireMock server, not ParaBank's live production server.");
        
        Row headerRow = sheet.createRow(1);
        String[] headers = {"Endpoint", "Avg Response Time (ms)", "Max Response Time (ms)", "Error Rate (%)", "Status"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 2;
        for (JMeterSummary record : records) {
            Row row = sheet.createRow(rowIndex++);
            
            long avgElapsed = record.totalElapsed / record.count;
            double errorRate = (double) record.errors / record.count * 100.0;
            boolean passed = avgElapsed < 3000 && errorRate <= 5.0;
            
            Cell nameCell = row.createCell(0);
            nameCell.setCellValue(record.endpoint);
            nameCell.setCellStyle(defaultDataStyle);
            
            Cell avgCell = row.createCell(1);
            avgCell.setCellValue(avgElapsed);
            avgCell.setCellStyle(defaultDataStyle);
            
            Cell maxCell = row.createCell(2);
            maxCell.setCellValue(record.maxElapsed);
            maxCell.setCellStyle(defaultDataStyle);
            
            Cell errorCell = row.createCell(3);
            errorCell.setCellValue(String.format("%.1f%%", errorRate));
            errorCell.setCellStyle(defaultDataStyle);
            
            Cell statusCell = row.createCell(4);
            statusCell.setCellValue(passed ? "PASS" : "FAIL");
            statusCell.setCellStyle(passed ? dataStylePass : dataStyleFail);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            if (sheet.getColumnWidth(i) > 255 * 256) {
                sheet.setColumnWidth(i, 255 * 256);
            }
        }
    }
}
