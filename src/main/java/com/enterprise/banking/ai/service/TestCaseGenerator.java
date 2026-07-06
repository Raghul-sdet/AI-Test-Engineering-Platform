package com.enterprise.banking.ai.service;

import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.model.TestScenario;
import java.util.List;

/**
 * Business service layer exposing operations for AI Test Case generations.
 */
public interface TestCaseGenerator {
    /**
     * Synthesizes detailed step-by-step test cases for a given high-level scenario.
     *
     * @param scenario The domain scenario generated from Phase 1.
     * @return A list of structured TestCase domain elements.
     */
    List<TestCase> generateTestCases(TestScenario scenario);
}