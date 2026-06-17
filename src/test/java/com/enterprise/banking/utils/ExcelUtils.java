package com.enterprise.banking.utils;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

public class ExcelUtils {

    // === The TestNG DataProvider Bridge ===
    @DataProvider(name = "Login data")
    public static Object[][] getLoginData() {
        // CRITICAL FIX: The space in "Login data" is exact to match your Excel tab
        return getTestData("src/test/resources/TestData.xlsx", "Login data");
    }

    // === Your Flawless Excel Reading Logic ===
    public static Object[][] getTestData(String excelPath, String sheetName) {

        File file = new File(excelPath);

        if (!file.exists()) {
            throw new RuntimeException("Excel File is missing at path: " + excelPath);
        }

        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException("The sheet named '" + sheetName + "' was not found inside the Excel file.");
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

            System.out.println("Rows = " + rowCount);
            System.out.println("Cols = " + colCount);

            Object[][] data = new Object[rowCount - 1][colCount];

            for (int i = 1; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    if (sheet.getRow(i) == null || sheet.getRow(i).getCell(j) == null) {
                        data[i - 1][j] = "";
                    } else {
                        data[i - 1][j] = sheet.getRow(i).getCell(j).toString().trim();
                    }
                }
            }

            return data;

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while reading Excel data: " + e.getMessage());
        }
    }
}