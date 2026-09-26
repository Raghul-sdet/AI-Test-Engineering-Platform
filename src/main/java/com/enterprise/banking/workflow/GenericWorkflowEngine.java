package com.enterprise.banking.workflow;

import com.enterprise.banking.config.SiteProfileLoader;
import com.enterprise.banking.config.WorkflowStep;
import com.enterprise.banking.utils.LocatorParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericWorkflowEngine {

    public void execute(WebDriver driver, SiteProfileLoader profileLoader, String workflowName) {
        List<WorkflowStep> steps = profileLoader.getWorkflowSteps(workflowName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        for (int i = 0; i < steps.size(); i++) {
            WorkflowStep step = steps.get(i);
            String action = step.getAction();
            
            try {
                if ("navigate".equalsIgnoreCase(action)) {
                    String url = resolvePlaceholders(step.getUrl(), profileLoader);
                    driver.get(url);
                } else {
                    String locatorStr = resolvePlaceholders(step.getLocator(), profileLoader);
                    
                    if (step.getOptional() && (locatorStr == null || locatorStr.trim().isEmpty())) {
                        System.out.println("Skipping optional step " + i + " due to missing locator");
                        continue;
                    }
                    
                    WebElement element = null;
                    try {
                        element = wait.until(ExpectedConditions.presenceOfElementLocated(LocatorParser.parseLocator(locatorStr)));
                    } catch (Exception e) {
                        if (step.getOptional()) {
                            System.out.println("Skipping optional step " + i + " due to element not found");
                            continue;
                        }
                        throw e;
                    }

                    if ("type".equalsIgnoreCase(action)) {
                        String value = resolvePlaceholders(step.getValue(), profileLoader);
                        if ("select".equalsIgnoreCase(step.getInputType())) {
                            Select dropdown = new Select(element);
                            dropdown.selectByVisibleText(value);
                        } else {
                            element = wait.until(ExpectedConditions.visibilityOf(element));
                            element.sendKeys(value);
                        }
                    } else if ("click".equalsIgnoreCase(action)) {
                        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                    } else if ("assertVisible".equalsIgnoreCase(action)) {
                        element = wait.until(ExpectedConditions.visibilityOf(element));
                        if (!element.isDisplayed()) {
                            throw new RuntimeException("Element not visible");
                        }
                    } else if ("assertTextContains".equalsIgnoreCase(action)) {
                        element = wait.until(ExpectedConditions.visibilityOf(element));
                        String expected = resolvePlaceholders(step.getValue(), profileLoader);
                        if (!element.getText().contains(expected)) {
                            throw new RuntimeException("Text '" + element.getText() + "' does not contain '" + expected + "'");
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Step " + i + " [" + action + "] failed: " + e.getMessage(), e);
            }
        }
    }

    private String resolvePlaceholders(String input, SiteProfileLoader profile) {
        if (input == null) return null;
        if (input.contains("${baseUrl}")) {
            input = input.replace("${baseUrl}", profile.getBaseUrl());
        }
        
        Pattern p = Pattern.compile("\\$\\{testData\\.([^}]+)\\}");
        Matcher m = p.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String key = m.group(1);
            m.appendReplacement(sb, profile.getTestData(key));
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
