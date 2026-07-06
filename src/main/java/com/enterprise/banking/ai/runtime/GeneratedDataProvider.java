package com.enterprise.banking.ai.runtime;

import org.apache.poi.ss.usermodel.*;
import org.testng.annotations.DataProvider;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic DataProvider that supplies execution parameters to generated tests
 * directly from the AI-generated Excel Test Design document.
 */
public class GeneratedDataProvider {

    private static final String EXCEL_PATH = "AI_Test_Design.xlsx";

    @DataProvider(name = "aiGeneratedData")
    public static Object[][] getGeneratedTestData(Method method) {
        List<Object[]> testData = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(EXCEL_PATH);
             Workbook workbook = WorkbookFactory.create(fis)) {
             
            Sheet sheet = workbook.getSheet("Test Steps");
            if (sheet == null) return new Object[][]{};

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; 
                
                Cell dataCell = row.getCell(3);
                
                if (dataCell != null) {
                    String data = dataCell.getStringCellValue();
                    
                    // Basic Phase 9 logic: If data contains '=', extract the value.
                    if (data != null && data.contains("=")) {
                        String value = data.split("=")[1].trim();
                        testData.add(new Object[]{value});
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[DATA-PROVIDER] Error reading test data: " + e.getMessage());
        }
        
        // Fallback to empty array if no data found to prevent TestNG crashes
        if (testData.isEmpty()) {
            return new Object[][] { { "default_value" } };
        }
        
        return testData.toArray(new Object[0][0]);
    }
}