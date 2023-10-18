package ru.egorov.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The database migration service class.
 */
public class DBMigrationService {

    private final DBConnectionProvider connectionProvider;
    private final String schemaName;
    private final String changeLogFile;

    public DBMigrationService(DBConnectionProvider connectionProvider, String schemaName, String changeLogFile) {
        this.connectionProvider = connectionProvider;
        this.schemaName = processedSchemaName(schemaName);
        this.changeLogFile = changeLogFile;
    }

    /**
     * Performs database migration.
     *
     */
    public void migration() {
        try(Connection connection = connectionProvider.getConnection()) {
            createSchemaForMigration(connection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(schemaName);
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void createSchemaForMigration(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        statement.executeUpdate(sql);
        statement.close();
    }

    private String processedSchemaName(String schemaName) {
        return schemaName.split("[^\\p{L}]")[0];
    }
}
