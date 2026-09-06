package com.enterprise.banking.ai.exploration.discovery;

import java.util.ArrayList;
import java.util.List;

/**
 * Identifies interactable elements within the current viewport (Buttons, Links, Inputs).
 */
public class ElementDiscoveryEngine {

    public List<String> discoverInteractables(String domSource) {
        List<String> elements = new ArrayList<>();
        if (domSource == null) return elements;
        
        if (domSource.contains("<button")) elements.add("BUTTON_ELEMENTS");
        if (domSource.contains("<a href")) elements.add("HYPERLINKS");
        if (domSource.contains("<input")) elements.add("INPUT_FIELDS");
        
        return elements;
    }
}