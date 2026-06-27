package com.enterprise.banking.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    
    // Saves the DB file into the target folder so it gets wiped clean automatically on 'mvn clean'
    // AUTO_SERVER=TRUE allows seamless parallel thread access
    private static final String DB_URL = "jdbc:h2:file:./target/h2db/automation_db;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    /**
     * Provides a fresh, isolated database connection for the current thread.
     * @return java.sql.Connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}