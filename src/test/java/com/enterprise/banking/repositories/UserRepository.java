package com.enterprise.banking.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Enterprise H2 Repository
 *
 * Responsible for:
 * 1. Database initialization & Auto-Healing
 * 2. Persisting users
 * 3. Reading fully data-driven test rows directly from the DB
 */
public class UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    private static final String DB_URL =
            "jdbc:h2:file:./target/h2db/test_users_db;AUTO_SERVER=TRUE";

    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    /**
     * Auto-Healing Logic: Creates table with DEFAULT values for Parabank testing.
     */
    private static void ensureTableExists(Connection connection) throws Exception {
        String createTable = """
                CREATE TABLE IF NOT EXISTS test_users(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    transfer_amount VARCHAR(50) DEFAULT '10',
                    search_amount VARCHAR(50) DEFAULT '10',
                    search_date VARCHAR(50) DEFAULT '11-20-2023',
                    search_trans_id VARCHAR(50) DEFAULT '12345'
                )
                """;
        try (PreparedStatement statement = connection.prepareStatement(createTable)) {
            statement.execute();
        }
    }

    public static void initializeDatabase() {
        try (
                Connection connection =
                        DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
        ) {
            ensureTableExists(connection);
            LOGGER.info("TEST_USERS table initialized successfully.");
            System.out.println("==========================================");
            System.out.println("H2 Database Initialized Successfully");
            System.out.println("Table : TEST_USERS (With Default Data Columns)");
            System.out.println("==========================================");
        } catch (Exception exception) {
            LOGGER.error("Database initialization failed.", exception);
            throw new RuntimeException("Unable to initialize H2 database.", exception);
        }
    }

    public static void saveUser(String username, String password) {
        String insertQuery =
                "INSERT INTO test_users(username,password) VALUES (?,?)";

        try (
                Connection connection =
                        DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        ) {
            ensureTableExists(connection);

            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.executeUpdate();
                LOGGER.info("User saved : {}", username);
                System.out.println("Saved User : " + username);
            }
        } catch (Exception exception) {
            LOGGER.error("Failed to save user.", exception);
            throw new RuntimeException("Failed to save user.", exception);
        }
    }

    /**
     * Returns the complete 6-parameter row directly from the Database.
     */
    public static String[] getLatestUserForTest() {
        String selectQuery =
                "SELECT * FROM test_users ORDER BY id DESC LIMIT 1";

        try (
                Connection connection =
                        DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        ) {
            ensureTableExists(connection);

            try (PreparedStatement statement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    LOGGER.info("Latest user loaded : {}", username);
                    System.out.println("Latest User Extracted From DB: " + username);

                    // ULTIMATE FIX: Dynamically generate TODAY's date in MM-dd-yyyy format
                    // This ensures Parabank successfully finds the transaction created just now!
                    String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

                    return new String[]{
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("transfer_amount"),
                            resultSet.getString("search_amount"),
                            todayDate, // Replaced the hardcoded DB date with dynamic Today's Date!
                            resultSet.getString("search_trans_id")
                    };
                }

                throw new RuntimeException("No registered user found inside H2 database.");
            }
        } catch (Exception exception) {
            LOGGER.error("Failed retrieving latest user.", exception);
            throw new RuntimeException("Unable to retrieve latest user.", exception);
        }
    }
}