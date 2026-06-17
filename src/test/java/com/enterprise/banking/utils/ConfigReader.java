package com.enterprise.banking.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        properties = new Properties();
        // 1. Try Classpath (Standard Maven way)
        InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties");
        
        // 2. If Classpath fails, try physical file path in the workspace
        if (input == null) {
            try {
                String path = System.getProperty("user.dir") + "/src/test/resources/config.properties";
                input = new FileInputStream(path);
            } catch (IOException e) {
                throw new RuntimeException("CRITICAL: Could not find config.properties in classpath OR at " + System.getProperty("user.dir") + "/src/test/resources/config.properties");
            }
        }

        try {
            properties.load(input);
            input.close();
        } catch (IOException e) {
            throw new RuntimeException("Error reading config.properties");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}