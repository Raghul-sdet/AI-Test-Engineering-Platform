package com.enterprise.banking.config;

public class WorkflowStep {
    private String action;
    private String url;
    private String locator;
    private String inputType;
    private String value;
    private Boolean optional;

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getLocator() { return locator; }
    public void setLocator(String locator) { this.locator = locator; }

    public String getInputType() { return inputType; }
    public void setInputType(String inputType) { this.inputType = inputType; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public Boolean getOptional() { return optional != null ? optional : false; }
    public void setOptional(Boolean optional) { this.optional = optional; }
}
