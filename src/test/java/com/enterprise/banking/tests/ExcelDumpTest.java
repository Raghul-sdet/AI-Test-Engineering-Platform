package com.enterprise.banking.tests;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;

public class ExcelDumpTest {

    @Test
    public void dumpExcel() throws Exception {
        System.out.println("\n\n--- EXCEL DUMP START ---");
        File file = new File(System.getProperty("user.dir") + java.io.File.separator + "target" + java.io.File.separator + "reports" + java.io.File.separator + "Professional_Enterprise_Report.xlsx");
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
             
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("Sheet Name: " + sheet.getSheetName());
            
            for (Row row : sheet) {
                StringBuilder sb = new StringBuilder();
                for (int i=0; i<9; i++) { // headers are 9 columns
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    sb.append(getCellValue(cell)).append(" | ");
                }
                System.out.println(sb.toString());
            }
        }
        System.out.println("--- EXCEL DUMP END ---\n\n");
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }
}
