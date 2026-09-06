package com.enterprise.banking.ai;

import com.enterprise.banking.ai.export.DatabaseToExcelExporter;
import com.enterprise.banking.ai.model.TestCase;
import com.enterprise.banking.ai.model.TestPlan;
import com.enterprise.banking.ai.model.TestScenario;

import java.util.Arrays;

public class ExcelReportTest {
    public static void main(String[] args) {
        System.out.println("Generating Professional Enterprise Excel Report...");

        TestPlan plan = new TestPlan();
        plan.setPlanId("PLAN-001");
        plan.setRequirementId("REQ-8902");
        plan.setFeatureName("Fund Transfer API");

        TestScenario scenario1 = new TestScenario();
        scenario1.setScenarioId("SCENARIO-1");
        scenario1.setScenarioTitle("Successful Internal Transfer");
        
        TestCase tc1 = new TestCase();
        tc1.setTestCaseId("TC-001");
        tc1.setTestCaseTitle("Transfer $500 between internal accounts");
        tc1.setTestSteps("1. Login\n2. Go to Transfer\n3. Enter $500\n4. Confirm");
        tc1.setPriority("HIGH");
        tc1.setSeverity("CRITICAL");

        TestCase tc2 = new TestCase();
        tc2.setTestCaseId("TC-002");
        tc2.setTestCaseTitle("Transfer $1 between internal accounts");
        tc2.setTestSteps("1. Login\n2. Go to Transfer\n3. Enter $1\n4. Confirm");
        tc2.setPriority("LOW");
        tc2.setSeverity("MINOR");

        scenario1.setTestCases(Arrays.asList(tc1, tc2));

        TestScenario scenario2 = new TestScenario();
        scenario2.setScenarioId("SCENARIO-2");
        scenario2.setScenarioTitle("Failed Transfer Insufficient Funds");
        
        TestCase tc3 = new TestCase();
        tc3.setTestCaseId("TC-003");
        tc3.setTestCaseTitle("Transfer $999999 between internal accounts");
        tc3.setTestSteps("1. Login\n2. Go to Transfer\n3. Enter $999999\n4. Confirm");
        tc3.setPriority("MEDIUM");
        tc3.setSeverity("MAJOR");

        scenario2.setTestCases(Arrays.asList(tc3));

        plan.setScenarios(Arrays.asList(scenario1, scenario2));

        DatabaseToExcelExporter exporter = new DatabaseToExcelExporter();
        String path = System.getProperty("user.dir") + java.io.File.separator + "target" + java.io.File.separator + "reports" + java.io.File.separator + "ExcelReportTest_output.xlsx";
        exporter.exportToExcel(plan, path);

        System.out.println("Report successfully generated at: " + path);
    }
}
