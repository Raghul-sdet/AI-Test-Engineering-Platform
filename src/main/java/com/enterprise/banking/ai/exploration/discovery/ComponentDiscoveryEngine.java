package com.enterprise.banking.ai.exploration.discovery;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes DOM blocks to identify complex components (Tables, Modals, Forms).
 */
public class ComponentDiscoveryEngine {

    public List<String> discoverComponents(String domSource) {
        List<String> components = new ArrayList<>();
        if (domSource == null) return components;
        
        if (domSource.contains("<form")) components.add("FORM_BLOCK");
        if (domSource.contains("<table")) components.add("DATA_TABLE");
        if (domSource.contains("role=\"dialog\"") || domSource.contains("modal")) components.add("MODAL_DIALOG");
        
        return components;
    }
}