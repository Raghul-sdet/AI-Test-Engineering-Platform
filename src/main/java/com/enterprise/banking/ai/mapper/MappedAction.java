package com.enterprise.banking.ai.mapper;

import com.enterprise.banking.ai.dom.model.DOMElement;
import java.util.List;

/**
 * Represents a successfully mapped action containing the inferred method details,
 * the target DOM element, and any required parameters.
 */
public record MappedAction(
        String methodName,
        String actionType, 
        DOMElement targetElement,
        List<String> parameters,
        double confidenceScore
) {}