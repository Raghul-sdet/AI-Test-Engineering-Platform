package com.enterprise.banking.ai.model;

public record TestStep(
        int stepNumber, 
        String action, 
        String testData, 
        String expectedResult
) {}