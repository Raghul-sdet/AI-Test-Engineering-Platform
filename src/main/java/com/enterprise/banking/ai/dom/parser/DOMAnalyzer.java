package com.enterprise.banking.ai.dom.parser;

import com.enterprise.banking.ai.dom.model.DOMElement;
import com.enterprise.banking.ai.dom.model.LocatorCandidate;
import com.enterprise.banking.ai.dom.service.LocatorRankingEngine;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses the live Document Object Model to extract automation-relevant elements.
 * Updated to capture value attributes for input-based buttons.
 */
public class DOMAnalyzer {

    private final LocatorRankingEngine rankingEngine;

    public DOMAnalyzer() {
        this.rankingEngine = new LocatorRankingEngine();
    }

    /**
     * Scans the provided WebDriver session for actionable HTML elements.
     *
     * @param driver The active WebDriver session.
     * @return A repository of structured DOM metadata.
     */
    public List<DOMElement> extractAutomationRelevantElements(WebDriver driver) {
        List<DOMElement> elementsMetadata = new ArrayList<>();
        String targetElementsSelector = "input, button, a, select, textarea";
        List<WebElement> rawElements = driver.findElements(By.cssSelector(targetElementsSelector));

        for (WebElement element : rawElements) {
            try {
                String tagName = element.getTagName().toLowerCase();
                String id = element.getAttribute("id");
                String name = element.getAttribute("name");
                String className = element.getAttribute("class");
                String placeholder = element.getAttribute("placeholder");
                String text = element.getText();
                String value = element.getAttribute("value");

                boolean isVisible = element.isDisplayed();
                boolean isInput = tagName.equals("input") || tagName.equals("textarea") || tagName.equals("select");
                boolean isClickable = element.isEnabled() && (tagName.equals("button") || tagName.equals("a") || isInput);

                if (!isVisible && !isInput) continue;

                List<LocatorCandidate> candidates = rankingEngine.generateRankedLocators(element, tagName);
                LocatorCandidate priority = candidates.isEmpty() ? null : candidates.get(0);
                
                String elementName = determineElementName(tagName, id, name, text, placeholder, value);

                DOMElement domElement = new DOMElement(
                        elementName, tagName, id, name, className, placeholder, text,
                        isClickable, isInput, isVisible, candidates, priority
                );

                elementsMetadata.add(domElement);

            } catch (Exception e) {
                // Silently swallow StaleElementExceptions during scanning
            }
        }
        return elementsMetadata;
    }

    private String determineElementName(String tag, String id, String name, String text, String placeholder, String value) {
        if (text != null && !text.isBlank()) return text.trim();
        if (value != null && !value.isBlank()) return value.trim(); // Added support for input buttons
        if (placeholder != null && !placeholder.isBlank()) return placeholder;
        if (name != null && !name.isBlank()) return name;
        if (id != null && !id.isBlank()) return id;
        return tag + "Element";
    }
}