package com.enterprise.banking.ai.dom.model;

import java.util.List;

/**
 * A structured metadata repository representing an automation-relevant HTML element.
 */
public record DOMElement(
        String elementName,
        String tagName,
        String id,
        String name,
        String className,
        String placeholder,
        String text,
        boolean isClickable,
        boolean isInput,
        boolean isVisible,
        List<LocatorCandidate> allLocators,
        LocatorCandidate priorityLocator
) {}