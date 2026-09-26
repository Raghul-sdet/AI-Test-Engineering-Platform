package com.enterprise.banking.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.Map;

public class SiteProfileLoader {

    private final String profileName;
    private final SiteProfile profile;

    public SiteProfileLoader() {
        this.profileName = System.getProperty("site.profile", "paraBank");
        this.profile = loadProfile(this.profileName);
    }
    
    public SiteProfileLoader(String profileName) {
        this.profileName = profileName;
        this.profile = loadProfile(this.profileName);
    }

    private SiteProfile loadProfile(String name) {
        String fileName = "site-profiles/" + name + ".yaml";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("Failed to load site profile: Profile file '" + fileName + "' does not exist in classpath.");
        }

        Yaml yaml = new Yaml(new Constructor(SiteProfile.class, new LoaderOptions()));
        return yaml.load(inputStream);
    }

    public String getBaseUrl() {
        if (profile.getBaseUrl() == null) {
            throw new IllegalArgumentException("Key 'baseUrl' is missing in profile '" + profileName + "'");
        }
        return profile.getBaseUrl();
    }

    public String getApiBaseUrl() {
        if (profile.getApiBaseUrl() == null) {
            throw new IllegalArgumentException("Key 'apiBaseUrl' is missing in profile '" + profileName + "'");
        }
        return profile.getApiBaseUrl();
    }

    public String getLocator(String workflow, String elementRole) {
        if (profile.getLocators() == null || !profile.getLocators().containsKey(workflow)) {
            throw new IllegalArgumentException("Locator workflow '" + workflow + "' is missing in profile '" + profileName + "'");
        }
        Map<String, String> workflowLocators = profile.getLocators().get(workflow);
        if (!workflowLocators.containsKey(elementRole)) {
            throw new IllegalArgumentException("Locator element '" + elementRole + "' under workflow '" + workflow + "' is missing in profile '" + profileName + "'");
        }
        return workflowLocators.get(elementRole);
    }

    public Map<String, String> getApiEndpoint(String operation) {
        if (profile.getApiEndpoints() == null || !profile.getApiEndpoints().containsKey(operation)) {
            throw new IllegalArgumentException("API endpoint operation '" + operation + "' is missing in profile '" + profileName + "'");
        }
        return profile.getApiEndpoints().get(operation);
    }

    public String getTestData(String fixtureName) {
        if (profile.getTestData() == null || !profile.getTestData().containsKey(fixtureName)) {
            throw new IllegalArgumentException("Test data fixture '" + fixtureName + "' is missing in profile '" + profileName + "'");
        }
        return profile.getTestData().get(fixtureName);
    }

    public void setTestData(String key, String value) {
        if (profile.getTestData() == null) {
            profile.setTestData(new java.util.HashMap<>());
        }
        profile.getTestData().put(key, value);
    }

    public java.util.List<WorkflowStep> getWorkflowSteps(String workflowName) {
        if (profile.getWorkflows() == null || !profile.getWorkflows().containsKey(workflowName)) {
            throw new IllegalArgumentException("Workflow '" + workflowName + "' is missing in profile '" + profileName + "'");
        }
        return profile.getWorkflows().get(workflowName).getSteps();
    }
}
