package ru.egorov.config;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * The database connection provider class.
 */
public class DBConnectionProvider {
    private final String url;
    private final String username;
    private final String password;
    private final String driver;

    public DBConnectionProvider(String url, String username, String password, String driver) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driver = driver;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Open connection to database.
     *
     * @return the connection
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
