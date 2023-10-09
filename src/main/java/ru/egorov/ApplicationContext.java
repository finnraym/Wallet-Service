package ru.egorov;

import ru.egorov.controller.MainController;
import ru.egorov.dao.PlayerDAO;
import ru.egorov.dao.TransactionDAO;
import ru.egorov.dao.impl.InMemoryPlayerDAO;
import ru.egorov.dao.impl.InMemoryTransactionDAO;
import ru.egorov.in.ConsoleInputData;
import ru.egorov.model.Player;
import ru.egorov.out.ConsoleOutputData;
import ru.egorov.service.PlayerService;
import ru.egorov.service.SecurityService;
import ru.egorov.service.TransactionService;
import ru.egorov.service.impl.PlayerServiceImpl;
import ru.egorov.service.impl.SecurityServiceImpl;
import ru.egorov.service.impl.TransactionServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private static final Map<String, Object> CONTEXT = new HashMap<>();

    public static void loadContext() {
        loadDAOLayer();
        loadServiceLayer();
        loadControllers();
        loadInputOutputLayer();
    }

    public static void loadAuthorizePlayer(Player player) {
        CONTEXT.put("authorize", player);
    }

    public static void cleanAuthorizePlayer() {
        CONTEXT.remove("authorize");
    }

    public static Player getAuthorizePlayer() {
        return (Player) CONTEXT.get("authorize");
    }

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
        CONTEXT.put("playerDAO", new InMemoryPlayerDAO());
        CONTEXT.put("transactionDAO", new InMemoryTransactionDAO());
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
