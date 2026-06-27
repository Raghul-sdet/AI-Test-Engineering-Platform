package com.enterprise.banking.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtility {

    // ThreadLocal ensures multiple parallel test threads do not share or clash on database connections
    private static ThreadLocal<Connection> connectionLocal = new ThreadLocal<>();

    /**
     * Opens a thread-safe database connection.
     */
    public static void openConnection() {
        if (connectionLocal.get() == null) {
            try {
                String dbUrl = ConfigReader.getProperty("dbUrl");
                String dbUser = ConfigReader.getProperty("dbUsername");
                String dbPass = ConfigReader.getProperty("dbPassword");

                // CRITICAL FIX: Explicitly calling java.sql.DriverManager to prevent collision 
                // with our custom Selenium DriverManager class in the same package.
                Connection connection = java.sql.DriverManager.getConnection(dbUrl, dbUser, dbPass);
                connectionLocal.set(connection);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to the database: " + e.getMessage());
            }
        }
    }

    /**
     * Executes a SELECT query and returns the results as a List of Maps.
     * This disconnected architecture prevents ResultSet closed exceptions.
     */
    public static List<Map<String, Object>> executeQuery(String query, Object... params) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        
        try (PreparedStatement stmt = connectionLocal.get().prepareStatement(query)) {
            
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> rowMap = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        rowMap.put(metaData.getColumnName(i).toLowerCase(), rs.getObject(i));
                    }
                    resultList.add(rowMap);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing SELECT query: " + e.getMessage());
        }
        return resultList;
    }

    /**
     * Executes INSERT, UPDATE, or DELETE queries.
     */
    public static int executeUpdate(String query, Object... params) {
        try (PreparedStatement stmt = connectionLocal.get().prepareStatement(query)) {
            
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error executing UPDATE query: " + e.getMessage());
        }
    }

    /**
     * Closes the connection and cleans up the ThreadLocal memory.
     */
    public static void closeConnection() {
        Connection connection = connectionLocal.get();
        if (connection != null) {
            try {
                connection.close();
                connectionLocal.remove();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close database connection: " + e.getMessage());
            }
        }
    }
}