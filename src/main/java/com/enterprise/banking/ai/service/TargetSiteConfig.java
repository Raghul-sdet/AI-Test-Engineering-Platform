package com.enterprise.banking.ai.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TargetSiteConfig {
    public static String resolveTargetUrl() {
        Properties properties = new Properties();
        try {
            String path = System.getProperty("user.dir") + "/src/test/resources/config.properties";
            InputStream input = new FileInputStream(path);
            properties.load(input);
            input.close();
            return properties.getProperty("uiBaseUrl", "https://parabank.parasoft.com/parabank");
        } catch (IOException e) {
            System.err.println("WARNING: Could not read config.properties, using default uiBaseUrl");
            return "https://parabank.parasoft.com/parabank";
        }
    }
}
