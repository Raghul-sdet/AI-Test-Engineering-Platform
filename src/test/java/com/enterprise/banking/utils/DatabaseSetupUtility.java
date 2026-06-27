package com.enterprise.banking.utils;

public class DatabaseSetupUtility {

    public static void initializeDatabase() {
        System.out.println(">>> Initializing H2 Automation Database Schema...");
        DBUtility.openConnection();

        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(50) NOT NULL, " +
                "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_id INT PRIMARY KEY, " +
                "user_id INT, " +
                "balance DECIMAL(10,2), " +
                "FOREIGN KEY (user_id) REFERENCES users(id))";

        DBUtility.executeUpdate(createUsersTable);
        DBUtility.executeUpdate(createAccountsTable);

        DBUtility.closeConnection();
        System.out.println(">>> H2 Database Initialization Complete.");
    }
}