package com.enterprise.banking.utils;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriteUtils {

    /**
     * Appends dynamic user credentials and default test data for all 6 framework columns.
     */
    public static void appendCredentialsToExcel(String filePath, String sheetName, 
            String username, String password, String transferAmount, 
            String searchAmount, String searchDate, String searchTransId) {
        
        File file = new File(filePath);
        Workbook workbook;
        FileInputStream fis = null;

        try {
            if (file.exists()) {
                fis = new FileInputStream(file);
                workbook = WorkbookFactory.create(fis);
            } else {
                throw new IOException("Excel file does not exist at path: " + filePath);
            }

            Sheet sheet = workbook.getSheet(sheetName);
            
            // If sheet is completely new, create it and build the 6-column header
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Username");
                headerRow.createCell(1).setCellValue("Password");
                headerRow.createCell(2).setCellValue("TransferAmount");
                headerRow.createCell(3).setCellValue("SearchAmount");
                headerRow.createCell(4).setCellValue("SearchDate");
                headerRow.createCell(5).setCellValue("SearchTransactionID");
            }

            int nextRowIndex = sheet.getLastRowNum() + 1;
            Row newRow = sheet.createRow(nextRowIndex);

            // Write all 6 columns for the new test iteration
            newRow.createCell(0).setCellValue(username);
            newRow.createCell(1).setCellValue(password);
            newRow.createCell(2).setCellValue(transferAmount);
            newRow.createCell(3).setCellValue(searchAmount);
            newRow.createCell(4).setCellValue(searchDate);
            
            // We write a blank string for ID unless you specifically want to pass one, 
            // since Parabank generates IDs dynamically during the Transfer test.
            newRow.createCell(5).setCellValue(searchTransId != null ? searchTransId : "");

            if (fis != null) {
                fis.close();
            }

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            
            fos.close();
            workbook.close();

            System.out.println(">>> Successfully appended 6 columns of data to Excel for user: " + username);

        } catch (Exception e) {
            System.err.println("Failed to write credentials to Excel file.");
            e.printStackTrace();
        }
    }
}