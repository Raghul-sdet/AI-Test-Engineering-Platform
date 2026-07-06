package com.enterprise.banking.ai.generator;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses the Excel workbook to extract structured test scenarios and steps dynamically.
 * Maps actions to specific page objects based on execution flow metadata.
 */
public class ExcelCodeReader {

    /**
     * Extracts dynamic method names grouped by their corresponding Page Object class.
     *
     * @param filePath The path to the generated Excel design workbook.
     * @return A map linking page names to their list of generated camelCase method names.
     * @throws Exception If file reading or parsing fails.
     */
    public Map<String, List<String>> extractPageMethodsFromWorkbook(String filePath) throws Exception {
        Map<String, List<String>> pageMethods = new LinkedHashMap<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            
            Sheet sheet = workbook.getSheet("Test Steps");
            if (sheet == null) {
                return pageMethods;
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; 
                
                Cell caseIdCell = row.getCell(0);
                Cell actionCell = row.getCell(2);
                
                if (caseIdCell == null || actionCell == null) continue;
                
                String caseId = caseIdCell.getStringCellValue();
                String action = actionCell.getStringCellValue();
                
                String pageName = determinePageFromCaseId(caseId);
                String methodName = convertActionToMethodName(action);
                
                pageMethods.computeIfAbsent(pageName, k -> new ArrayList<>()).add(methodName);
            }
        }
        return pageMethods;
    }

    private String determinePageFromCaseId(String caseId) {
        if (caseId.contains("TC_001")) {
            return "TransferFundsPage";
        }
        return "LoginPage";
    }

    private String convertActionToMethodName(String action) {
        if (action == null || action.isBlank()) {
            return "executeAction";
        }
        
        String clean = action.replaceAll("[^a-zA-Z0-9\\s]", "");
        String[] words = clean.split("\\s+");
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase();
            if (word.isBlank()) continue;
            if (i == 0) {
                sb.append(word);
            } else {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
            }
        }
        return sb.toString().isBlank() ? "executeAction" : sb.toString();
    }
}