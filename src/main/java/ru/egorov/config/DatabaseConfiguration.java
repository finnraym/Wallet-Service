package ru.egorov.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import ru.egorov.ApplicationContext;

import java.sql.*;
import java.util.Properties;

public class DatabaseConfiguration {
    private static final String dbUrl;
    private static final String dbUser;
    private static final String dbPassword;
    private static final String dbDriver;
    private static final String liquibaseChangeLogFile;
    private static final String liquibaseSchemaName;

    static {
        Properties properties = ApplicationContext.getProperties();
        dbUrl = properties.getProperty("db.url");
        dbUser = properties.getProperty("db.user");
        dbPassword = properties.getProperty("db.password");
        dbDriver = properties.getProperty("db.driver");
        liquibaseChangeLogFile = properties.getProperty("liquibase.changeLogFile");
        liquibaseSchemaName = properties.getProperty("liquibase.schemaName");
    }

    public static void migration() {
        try(Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            createSchemaForMigration(connection);
            liquibaseUpdate(connection);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
    private static void liquibaseUpdate(Connection connection) throws LiquibaseException {
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        database.setLiquibaseSchemaName(liquibaseSchemaName);
        Liquibase liquibase = new Liquibase(liquibaseChangeLogFile, new ClassLoaderResourceAccessor(), database);
        liquibase.update();
    }

    private static void createSchemaForMigration(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String schemaName = processedSchemaName();
        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        statement.executeUpdate(sql);
        statement.close();
    }

    private static String processedSchemaName() {
        return liquibaseSchemaName.split("[^\\p{L}]")[0];
    }
}
