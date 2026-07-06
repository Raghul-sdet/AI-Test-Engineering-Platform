package com.enterprise.banking.ai.dom.service;

import com.enterprise.banking.ai.dom.model.LocatorCandidate;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Intelligent engine responsible for scoring and ranking locators
 * based on enterprise strict hierarchy rules.
 */
public class LocatorRankingEngine {

    /**
     * Generates and ranks locators for a given WebElement.
     * Hierarchy: id(1) > name(2) > data-testid(3) > aria-label(4) > css(5) > relative xpath(6).
     *
     * @param element The target WebElement.
     * @param tagName The tag name of the element.
     * @return A sorted list of locator candidates.
     */
    public List<LocatorCandidate> generateRankedLocators(WebElement element, String tagName) {
        List<LocatorCandidate> candidates = new ArrayList<>();

        String id = element.getAttribute("id");
        if (isValidAttribute(id)) {
            candidates.add(new LocatorCandidate("id", id, 1));
        }

        String name = element.getAttribute("name");
        if (isValidAttribute(name)) {
            candidates.add(new LocatorCandidate("name", name, 2));
        }

        String testId = element.getAttribute("data-testid");
        if (isValidAttribute(testId)) {
            candidates.add(new LocatorCandidate("css", "[data-testid='" + testId + "']", 3));
        }

        String ariaLabel = element.getAttribute("aria-label");
        if (isValidAttribute(ariaLabel)) {
            candidates.add(new LocatorCandidate("css", "[aria-label='" + ariaLabel + "']", 4));
        }

        String className = element.getAttribute("class");
        if (isValidAttribute(className) && !className.contains(" ")) {
            // Using a single class name as CSS if it doesn't contain spaces (to avoid fragile compound classes)
            candidates.add(new LocatorCandidate("css", "." + className, 5));
        }

        // Relative XPath generation (Fallback)
        if (isValidAttribute(name)) {
            candidates.add(new LocatorCandidate("xpath", "//" + tagName + "[@name='" + name + "']", 6));
        } else if (isValidAttribute(id)) {
            candidates.add(new LocatorCandidate("xpath", "//" + tagName + "[@id='" + id + "']", 6));
        }

        Collections.sort(candidates);
        return candidates;
    }

    private boolean isValidAttribute(String attr) {
        return attr != null && !attr.trim().isEmpty();
    }
}