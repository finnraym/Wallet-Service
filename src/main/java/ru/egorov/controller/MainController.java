package ru.egorov.controller;

import ru.egorov.exception.NotValidArgumentException;
import ru.egorov.model.Player;
import ru.egorov.model.Transaction;
import ru.egorov.service.PlayerService;
import ru.egorov.service.SecurityService;
import ru.egorov.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The type Main controller.
 */
public class MainController {

    private final TransactionService transactionService;
    private final SecurityService securityService;
    private final PlayerService playerService;

    /**
     * Instantiates a new Main controller.
     *
     * @param transactionService the transaction service
     * @param securityService    the security service
     * @param playerService      the player service
     */
    public MainController(TransactionService transactionService, SecurityService securityService, PlayerService playerService) {
        this.transactionService = transactionService;
        this.securityService = securityService;
        this.playerService = playerService;
    }

    /**
     * Register player.
     *
     * @param login    the login
     * @param password the password
     * @return the player
     */
    public Player register(String login, String password) {
        if (login == null || password == null || login.isEmpty() || login.isBlank() || password.isEmpty() || password.isBlank()) {
            throw new NotValidArgumentException("The password or login cannot be empty or consist of only spaces");
        }

        if (password.length() < 4 || password.length() > 32) {
            throw new NotValidArgumentException("The password must be between 4 and 32 characters long.");
        }

        return securityService.register(login, password);
    }

    /**
     * Authorize player.
     *
     * @param login    the login
     * @param password the password
     * @return the player
     */
    public Player authorize(String login, String password) {
        return securityService.authorization(login, password);
    }

    /**
     * Show balance big decimal.
     *
     * @param player the player
     * @return the big decimal
     */
    public BigDecimal showBalance(Player player) {
        return playerService.getPlayerBalance(player.getId());
    }

    /**
     * Show transactions history list.
     *
     * @param player the player
     * @return the list
     */
    public List<Transaction> showTransactionsHistory(Player player) {
        return transactionService.getPlayerHistory(player.getId());
    }

    /**
     * Debit transaction boolean.
     *
     * @param amount                the amount
     * @param transactionIdentifier the transaction identifier
     * @param player                the player
     * @return the boolean
     */
    public boolean debitTransaction(BigDecimal amount, UUID transactionIdentifier, Player player) {
        if (amount.signum() < 1) {
            throw new NotValidArgumentException("The transaction value cannot be less than or equal to 0.");
        }
        return transactionService.debit(amount, transactionIdentifier, player.getId());
    }

    /**
     * Credit transaction boolean.
     *
     * @param amount                the amount
     * @param transactionIdentifier the transaction identifier
     * @param player                the player
     * @return the boolean
     */
    public boolean creditTransaction(BigDecimal amount, UUID transactionIdentifier, Player player) {
        if (amount.signum() < 1) {
            throw new NotValidArgumentException("The transaction value cannot be less than or equal to 0.");
        }
        return transactionService.credit(amount, transactionIdentifier, player.getId());
    }
}
