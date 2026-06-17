package com.enterprise.banking.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        // Use the ClassLoader to find the file in the classpath
        // This works even when running inside Jenkins target/test-classes
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find config.properties in the classpath!");
            }
            
            properties = new Properties();
            properties.load(input);
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading configuration properties");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}