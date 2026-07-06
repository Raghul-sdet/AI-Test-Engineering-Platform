package com.enterprise.banking.ai.generator;

import java.util.List;
import java.util.Map;

/**
 * Orchestrates the conversion of Excel artifacts into dynamic context-aware Selenium architectures.
 */
public class CodeGenerationEngine {

    /**
     * Executes the end-to-end processing mapping model steps directly to source methods.
     *
     * @param excelPath The file path to the AI-generated Excel workbook.
     * @throws Exception If mapping execution fails.
     */
    public void runGenerationPipeline(String excelPath) throws Exception {
        ExcelCodeReader reader = new ExcelCodeReader();
        SeleniumCodeGenerator generator = new SeleniumCodeGenerator();

        // 1. Generate the foundational BaseTest for WebDriver management
        generator.generateBaseTest();

        // 2. Extract mapped methods from Excel
        Map<String, List<String>> pageMethodsMap = reader.extractPageMethodsFromWorkbook(excelPath);
        
        // 3. Generate dynamic Page Objects and Test classes
        for (Map.Entry<String, List<String>> entry : pageMethodsMap.entrySet()) {
            String pageName = entry.getKey();
            List<String> methods = entry.getValue();
            
            generator.generateDynamicPageObject(pageName, methods);
            generator.generateDynamicTestClass(pageName, methods);
        }
    }
}