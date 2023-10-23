package ru.egorov.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.egorov.config.DBConnectionProvider;
import ru.egorov.config.DBMigrationService;
import ru.egorov.dao.PlayerDAO;
import ru.egorov.dao.TransactionDAO;
import ru.egorov.dao.impl.JdbcPlayerDAO;
import ru.egorov.dao.impl.JdbcTransactionDAO;
import ru.egorov.in.mappers.PlayerMapper;
import ru.egorov.in.mappers.TransactionMapper;
import ru.egorov.in.security.JwtTokenProvider;
import ru.egorov.service.PlayerService;
import ru.egorov.service.SecurityService;
import ru.egorov.service.TransactionService;
import ru.egorov.service.impl.PlayerServiceImpl;
import ru.egorov.service.impl.SecurityServiceImpl;
import ru.egorov.service.impl.TransactionServiceImpl;

import java.io.*;
import java.util.Properties;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private Properties properties;
    private DBConnectionProvider connectionProvider;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        loadProperties(servletContext);
        databaseConfiguration(servletContext);
        serviceContextInit(servletContext);

        ObjectMapper jacksonMapper = new ObjectMapper();
        servletContext.setAttribute("jacksonMapper", jacksonMapper);
        servletContext.setAttribute("playerMapper", PlayerMapper.INSTANCE);
        servletContext.setAttribute("transactionMapper", TransactionMapper.INSTANCE);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private void databaseConfiguration(ServletContext servletContext) {
        String dbUrl = properties.getProperty("db.url");
        String dbUser = properties.getProperty("db.user");
        String dbPassword = properties.getProperty("db.password");
        String dbDriver = properties.getProperty("db.driver");
        connectionProvider = new DBConnectionProvider(dbUrl, dbUser, dbPassword, dbDriver);
        servletContext.setAttribute("connectionProvider", connectionProvider);

        String changeLogFile = properties.getProperty("liquibase.changeLogFile");
        String schemaName = properties.getProperty("liquibase.schemaName");

        DBMigrationService migrationService = new DBMigrationService(connectionProvider, schemaName, changeLogFile);
        migrationService.migration();
        servletContext.setAttribute("migrationService", migrationService);
    }

    private void serviceContextInit(ServletContext servletContext) {
        PlayerDAO playerDAO = new JdbcPlayerDAO(connectionProvider);
        TransactionDAO transactionDAO = new JdbcTransactionDAO(connectionProvider);

        PlayerService playerService = new PlayerServiceImpl(playerDAO);
        JwtTokenProvider tokenProvider = new JwtTokenProvider(
                properties.getProperty("jwt.secret"),
                Long.parseLong(properties.getProperty("jwt.access")),
                Long.parseLong(properties.getProperty("jwt.refresh")),
                playerService
        );

        SecurityService securityService = new SecurityServiceImpl(playerDAO, tokenProvider);
        TransactionService transactionService = new TransactionServiceImpl(transactionDAO, playerService);


        servletContext.setAttribute("tokenProvider", tokenProvider);
        servletContext.setAttribute("securityService", securityService);
        servletContext.setAttribute("playerService", playerService);
        servletContext.setAttribute("transactionService", transactionService);
    }

    private void loadProperties(ServletContext servletContext) {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(servletContext.getResourceAsStream("/WEB-INF/classes/application.properties"));
                servletContext.setAttribute("servletProperties", properties);
            }catch (FileNotFoundException e ) {
                throw new RuntimeException("Property file not found!");
            } catch (IOException e) {
                throw new RuntimeException("Error reading configuration file: " + e.getMessage());
            }
        }
    }
}
