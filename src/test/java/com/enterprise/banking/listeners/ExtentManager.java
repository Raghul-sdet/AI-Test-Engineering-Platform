package com.enterprise.banking.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    
    private static ExtentReports extent;

    public static ExtentReports createInstance() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/test-output/AutomationReport.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            
            sparkReporter.config().setDocumentTitle("Enterprise Banking Test Report");
            sparkReporter.config().setReportName("QA Automation Execution Results");
            sparkReporter.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            
            // System details for the dashboard
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Author", "QA Automation Engineer");
            extent.setSystemInfo("Environment", "QA");
        }
        return extent;
    }
}