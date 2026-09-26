package com.enterprise.banking.config;

import java.util.Map;

public class SiteProfile {
    private String siteName;
    private String baseUrl;
    private String apiBaseUrl;
    private Map<String, String> authentication;
    private Map<String, Map<String, String>> locators;
    private Map<String, Map<String, String>> apiEndpoints;
    private Map<String, String> testData;
    private Map<String, WorkflowConfig> workflows;

    public Map<String, WorkflowConfig> getWorkflows() { return workflows; }
    public void setWorkflows(Map<String, WorkflowConfig> workflows) { this.workflows = workflows; }

    public String getSiteName() { return siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getApiBaseUrl() { return apiBaseUrl; }
    public void setApiBaseUrl(String apiBaseUrl) { this.apiBaseUrl = apiBaseUrl; }

    public Map<String, String> getAuthentication() { return authentication; }
    public void setAuthentication(Map<String, String> authentication) { this.authentication = authentication; }

    public Map<String, Map<String, String>> getLocators() { return locators; }
    public void setLocators(Map<String, Map<String, String>> locators) { this.locators = locators; }

    public Map<String, Map<String, String>> getApiEndpoints() { return apiEndpoints; }
    public void setApiEndpoints(Map<String, Map<String, String>> apiEndpoints) { this.apiEndpoints = apiEndpoints; }

    public Map<String, String> getTestData() { return testData; }
    public void setTestData(Map<String, String> testData) { this.testData = testData; }
}
