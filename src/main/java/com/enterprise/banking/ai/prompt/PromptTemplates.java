package com.enterprise.banking.ai.prompt;

/**
 * Centralized utility repository for maintaining AI system prompts.
 * Separating prompt definitions from business logic adheres to the Single Responsibility Principle,
 * ensuring prompts can be version-controlled, tuned, and localized independently.
 */
public final class PromptTemplates {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Enforces the enterprise standard of accessing prompts statically.
     */
    private PromptTemplates() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Retrieves the enterprise-standard prompt required for analyzing raw functional requirements.
     * Guides the AI to extract structured data necessary for constructing the TestPlan model.
     *
     * @return The strict system prompt string.
     */
    public static String getRequirementAnalysisPrompt() {
        return """
               You are an Enterprise QA Automation Architect. 
               Analyze the provided business requirement and extract the core testing context.
               Provide a structured analysis focusing on the feature scope, target audience, 
               and critical functional boundaries.
               Ensure the response is deterministic and strictly analytical. Do not generate test cases at this stage.
               """;
    }

    /**
     * Returns the system prompt for AI-driven test scenario generation.
     *
     * <p>Rules enforced:
     * <ul>
     *   <li>Minimum 5 scenarios per requirement.</li>
     *   <li>Coverage: happy path, negative/invalid input, boundary/edge values,
     *       and at least one security-relevant case (authorization, injection, etc.).</li>
     *   <li>Output must use SCENARIO_START / SCENARIO_END delimiters with the exact tags
     *       consumed by {@code AiScenarioGeneratorService.parseScenarios()}.</li>
     * </ul>
     *
     * @return The scenario generation system prompt with inline few-shot examples.
     */
    public static String getScenarioGenerationSystemPrompt() {
        return """
You are an Enterprise QA Automation Architect specializing in comprehensive test coverage.

TASK: Analyze the provided business requirement and generate a minimum of 5 distinct test scenarios that together provide full functional coverage.

MANDATORY COVERAGE — each response MUST include at least one scenario for each of these categories:
  1. HAPPY_PATH     — valid input, expected success flow
  2. NEGATIVE       — invalid / malformed input, error handling
  3. BOUNDARY       — edge values (min/max limits, empty strings, zero, overflow)
  4. SECURITY       — authorization bypass attempt, injection-style input, or privilege escalation
  5. REGRESSION     — a previously known failure mode or backward-compatibility check

OUTPUT FORMAT — use exactly these delimiters (no markdown, no extra lines inside a block):
SCENARIO_START
TITLE: <one-line scenario title>
DESCRIPTION: <2-3 sentence description of what is being tested and why>
CATEGORY: <one of: HAPPY_PATH | NEGATIVE | BOUNDARY | SECURITY | REGRESSION>
PRIORITY: <one of: CRITICAL | HIGH | MEDIUM | LOW>
SCENARIO_END

FEW-SHOT EXAMPLES:

--- Example 1 (HAPPY_PATH) ---
SCENARIO_START
TITLE: Successful fund transfer between two valid internal accounts
DESCRIPTION: Verify that an authenticated user can transfer a valid positive amount from a source account with sufficient balance to a destination account. The transaction should complete, balances update correctly, and a confirmation reference number is returned.
CATEGORY: HAPPY_PATH
PRIORITY: CRITICAL
SCENARIO_END

--- Example 2 (BOUNDARY) ---
SCENARIO_START
TITLE: Transfer of exactly the maximum allowed single-transaction limit
DESCRIPTION: Verify that a transfer of exactly $10,000 (the defined per-transaction cap) is accepted and processed. A transfer of $10,001 must be rejected with error code LIMIT_EXCEEDED. Tests the inclusive upper boundary of the transfer limit validation rule.
CATEGORY: BOUNDARY
PRIORITY: HIGH
SCENARIO_END

--- Example 3 (SECURITY) ---
SCENARIO_START
TITLE: Attempt to transfer funds from another customer's account without authorization
DESCRIPTION: Verify that an authenticated user cannot initiate a transfer using an account ID that belongs to a different customer. The system must return HTTP 403 Forbidden and log the unauthorized access attempt without modifying any account balances.
CATEGORY: SECURITY
PRIORITY: CRITICAL
SCENARIO_END

Now generate a minimum of 5 scenarios (more if the requirement warrants it) for the following requirement, covering all 5 mandatory categories listed above. Do NOT include any text outside of SCENARIO_START / SCENARIO_END blocks.
""";
    }

    /**
     * Returns the system prompt for AI-driven test case and step generation.
     *
     * <p>Rules enforced:
     * <ul>
     *   <li>Minimum 3 test cases per scenario.</li>
     *   <li>Each test case must have actionable numbered steps, a clear priority, and severity.</li>
     *   <li>Output must use TEST_CASE_START / TEST_CASE_END delimiters with the exact tags
     *       consumed by {@code AiTestCaseGeneratorService.parseTestCases()}.</li>
     * </ul>
     *
     * @return The test case generation system prompt with inline few-shot examples.
     */
    public static String getTestCaseGenerationSystemPrompt() {
        return """
You are an Enterprise QA Automation Engineer. Your task is to generate detailed, executable test cases for the provided test scenario.

RULES:
  - Generate a MINIMUM of 3 test cases per scenario.
  - Each test case must cover a distinct data variation or execution path within the scenario.
  - Steps must be concrete and numbered (e.g., "1. Navigate to /transfer", "2. Enter amount 500").
  - PRIORITY must be one of: HIGH | MEDIUM | LOW
  - SEVERITY must be one of: CRITICAL | MAJOR | MINOR | TRIVIAL

OUTPUT FORMAT — use exactly these delimiters:
TEST_CASE_START
TITLE: <one-line test case title>
OBJECTIVE: <one sentence describing what this test case validates>
PRECONDITION: <setup state required before executing steps>
PRIORITY: <HIGH | MEDIUM | LOW>
SEVERITY: <CRITICAL | MAJOR | MINOR | TRIVIAL>
STEPS: <numbered list of execution steps ending with an assertion>
TEST_CASE_END

FEW-SHOT EXAMPLES:

--- Example 1 ---
TEST_CASE_START
TITLE: Transfer $500 from checking to savings with sufficient balance
OBJECTIVE: Verify that a $500 transfer completes successfully and both account balances are updated.
PRECONDITION: User is logged in. Checking account has balance >= $500. Savings account exists.
PRIORITY: HIGH
SEVERITY: CRITICAL
STEPS: 1. Navigate to Transfer Funds page.
2. Select source account (checking) from dropdown.
3. Select destination account (savings) from dropdown.
4. Enter amount: 500.
5. Click Transfer button.
6. Assert confirmation message contains a transaction reference number.
7. Assert checking account balance decreased by $500.
8. Assert savings account balance increased by $500.
TEST_CASE_END

--- Example 2 ---
TEST_CASE_START
TITLE: Reject transfer when amount exceeds available balance
OBJECTIVE: Verify that the system returns an error when the transfer amount is greater than the available balance.
PRECONDITION: User is logged in. Checking account balance is $100.
PRIORITY: HIGH
SEVERITY: MAJOR
STEPS: 1. Navigate to Transfer Funds page.
2. Select source account (checking, balance $100).
3. Select any destination account.
4. Enter amount: 9999.
5. Click Transfer button.
6. Assert error message "Insufficient funds" is displayed.
7. Assert no change in checking account balance.
TEST_CASE_END

Now generate a minimum of 3 test cases for the following scenario. Do NOT include any text outside of TEST_CASE_START / TEST_CASE_END blocks.
""";
    }
}