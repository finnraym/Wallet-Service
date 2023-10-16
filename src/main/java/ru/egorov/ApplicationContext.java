package ru.egorov;

import ru.egorov.controller.MainController;
import ru.egorov.dao.PlayerDAO;
import ru.egorov.dao.TransactionDAO;
import ru.egorov.dao.impl.JdbcPlayerDAO;
import ru.egorov.dao.impl.JdbcTransactionDAO;
import ru.egorov.in.ConsoleInputData;
import ru.egorov.model.Player;
import ru.egorov.out.ConsoleOutputData;
import ru.egorov.service.PlayerService;
import ru.egorov.service.SecurityService;
import ru.egorov.service.TransactionService;
import ru.egorov.service.impl.PlayerServiceImpl;
import ru.egorov.service.impl.SecurityServiceImpl;
import ru.egorov.service.impl.TransactionServiceImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The type Application context.
 */
public class ApplicationContext {
    private static final Map<String, Object> CONTEXT = new HashMap<>();
    private static Properties properties;
    private static final String PROPERTIES_FILEPATH = getPropertiesFilepath();

    /**
     * Load context.
     */
    public static void loadContext() {
        loadProperties();
        loadDAOLayer();
        loadServiceLayer();
        loadControllers();
        loadInputOutputLayer();
    }

    public static Properties getProperties() {
        if (properties == null) loadProperties();
        return properties;
    }

    private static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream stream = Files.newInputStream(Paths.get(PROPERTIES_FILEPATH))) {
                properties.load(stream);
            } catch (IOException e) {
                throw new RuntimeException("Error reading configuration file: " + e.getMessage());
            }
        }
    }
    private static String getPropertiesFilepath() {
        return "src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties";
    }

    /**
     * Load authorize player.
     *
     * @param player the player
     */
    public static void loadAuthorizePlayer(Player player) {
        CONTEXT.put("authorize", player);
    }

    /**
     * Clean authorize player.
     */
    public static void cleanAuthorizePlayer() {
        CONTEXT.remove("authorize");
    }

    /**
     * Gets authorize player.
     *
     * @return the authorize player
     */
    public static Player getAuthorizePlayer() {
        return (Player) CONTEXT.get("authorize");
    }

    /**
     * Gets bean.
     *
     * @param beanName the bean name
     * @return the bean
     */
    public static Object getBean(String beanName) {
        return CONTEXT.get(beanName);
    }

    private static void loadControllers() {
        MainController controller = new MainController(
                (TransactionService) CONTEXT.get("transactionService"),
                (SecurityService) CONTEXT.get("securityService"),
                (PlayerService) CONTEXT.get("playerService")
        );
        CONTEXT.put("controller", controller);
    }

    private static void loadInputOutputLayer() {
        CONTEXT.put("input", new ConsoleInputData());
        CONTEXT.put("output", new ConsoleOutputData());
    }

    private static void loadDAOLayer() {
        CONTEXT.put("playerDAO", new JdbcPlayerDAO());
        CONTEXT.put("transactionDAO", new JdbcTransactionDAO());
    }

    private static void loadServiceLayer() {
        PlayerService playerService = new PlayerServiceImpl((PlayerDAO) CONTEXT.get("playerDAO"));
        CONTEXT.put("playerService", playerService);
        SecurityService securityService = new SecurityServiceImpl((PlayerDAO) CONTEXT.get("playerDAO"));
        CONTEXT.put("securityService", securityService);
        TransactionService transactionService = new TransactionServiceImpl(
                (TransactionDAO) CONTEXT.get("transactionDAO"),
                (PlayerService) CONTEXT.get("playerService")
        );
        CONTEXT.put("transactionService", transactionService);
    }

}
