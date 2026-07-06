package com.enterprise.banking.ai.dom.service;

import com.enterprise.banking.ai.dom.model.DOMElement;
import com.enterprise.banking.ai.dom.parser.DOMAnalyzer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

/**
 * Orchestrator service that manages the browser session and executes DOM analysis.
 * Built to be thread-safe per execution request.
 */
public class DOMExtractionService {

    /**
     * Executes the DOM intelligence extraction pipeline on a target URL.
     *
     * @param targetUrl The live webpage to analyze.
     * @return The complete metadata repository of the parsed DOM.
     */
    public List<DOMElement> executeDomAnalysis(String targetUrl) {
        System.out.println("\n[DOM-ENGINE] Initiating DOM analysis for URL: " + targetUrl);
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Ensures silent execution
        WebDriver driver = new ChromeDriver(options);
        
        try {
            driver.get(targetUrl);
            // Allow JS execution to settle
            Thread.sleep(2000); 

            DOMAnalyzer analyzer = new DOMAnalyzer();
            List<DOMElement> extractedElements = analyzer.extractAutomationRelevantElements(driver);
            
            printExtractionLogs(extractedElements);
            return extractedElements;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during DOM analysis settling time", e);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private void printExtractionLogs(List<DOMElement> elements) {
        int totalInputs = 0;
        int totalButtons = 0;
        int totalLinks = 0;
        int totalDropdowns = 0;

        for (DOMElement el : elements) {
            switch (el.tagName()) {
                case "input":
                case "textarea":
                    totalInputs++;
                    break;
                case "button":
                    totalButtons++;
                    break;
                case "a":
                    totalLinks++;
                    break;
                case "select":
                    totalDropdowns++;
                    break;
            }
        }

        System.out.println("--------------------------------------------------");
        System.out.println("          DOM ANALYSIS SUMMARY REPORT             ");
        System.out.println("--------------------------------------------------");
        System.out.println("Total elements scanned : " + elements.size());
        System.out.println("Total input fields     : " + totalInputs);
        System.out.println("Total buttons          : " + totalButtons);
        System.out.println("Total links            : " + totalLinks);
        System.out.println("Total dropdowns        : " + totalDropdowns);
        System.out.println("--------------------------------------------------");

        System.out.println("\n[DOM-ENGINE] Top 5 Discovered Priority Locators:");
        elements.stream()
                .filter(e -> e.priorityLocator() != null)
                .limit(5)
                .forEach(e -> System.out.println("Element: " + e.elementName() + " -> " + e.priorityLocator()));
    }
}