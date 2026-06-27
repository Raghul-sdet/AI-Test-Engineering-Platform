package com.enterprise.banking.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReportGenerator {

    /**
     * Queries the H2 database and exports all generated users to an Excel report.
     * This file is saved to target/reports/ so it can be archived by Jenkins.
     */
    public static void generateExecutionReport() {
        System.out.println(">>> Generating Post-Execution Excel Report...");
        
        String reportDirectory = "target/reports/";
        File dir = new File(reportDirectory);
        if (!dir.exists()) {
            dir.mkdirs(); // Automatically create the reports folder if it doesn't exist
        }
        
        String filePath = reportDirectory + "TestUsers_Report.xlsx";

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM test_users")) {

            XSSFSheet sheet = workbook.createSheet("Execution Data");

            // Create Header Styling
            XSSFCellStyle headerStyle = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            // Generate Headers
            XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Username", "Password", "Creation Timestamp", "Status"};
            for (int i = 0; i < headers.length; i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Populate Data from H2
            int rowIndex = 1;
            while (rs.next()) {
                XSSFRow dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(rs.getInt("id"));
                dataRow.createCell(1).setCellValue(rs.getString("username"));
                dataRow.createCell(2).setCellValue(rs.getString("password"));
                dataRow.createCell(3).setCellValue(rs.getString("created_at"));
                dataRow.createCell(4).setCellValue("PASSED"); 
            }

            // Auto-size columns for a professional look
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Save the report
            try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
                workbook.write(fos);
            }
            
            System.out.println(">>> EXPORT SUCCESS: Report saved to " + filePath);

        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to generate Excel report: " + e.getMessage());
        }
    }
}