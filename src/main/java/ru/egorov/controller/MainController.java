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

public class MainController {

    private final TransactionService transactionService;
    private final SecurityService securityService;
    private final PlayerService playerService;

    public MainController(TransactionService transactionService, SecurityService securityService, PlayerService playerService) {
        this.transactionService = transactionService;
        this.securityService = securityService;
        this.playerService = playerService;
    }

    public Player register(String login, String password) {
        if (login == null || password == null || login.isEmpty() || login.isBlank() || password.isEmpty() || password.isBlank()) {
            throw new NotValidArgumentException("Пароль или логин не могут быть пустыми или состоять из одних пробелов");
        }

        if (password.length() < 4 || password.length() > 32) {
            throw new NotValidArgumentException("Пароль должен быть длиной от 4 до 32 символов.");
        }

        return securityService.register(login, password);
    }

    public Player authorize(String login, String password) {
        return securityService.authorization(login, password);
    }

    public BigDecimal showBalance(Player player) {
        return playerService.getPlayerBalance(player.getId());
    }

    public List<Transaction> showTransactionsHistory(Player player) {
        return transactionService.getPlayerHistory(player.getId());
    }

    public boolean debitTransaction(BigDecimal amount, UUID transactionIdentifier, Player player) {
        if (amount.signum() < 1) {
            throw new NotValidArgumentException("Значение транзакции не может быть ниже или равна 0");
        }
        return transactionService.debit(amount, transactionIdentifier, player.getId());
    }

    public boolean creditTransaction(BigDecimal amount, UUID transactionIdentifier, Player player) {
        if (amount.signum() < 1) {
            throw new NotValidArgumentException("Значение транзакции не может быть ниже или равна 0");
        }
        return transactionService.credit(amount, transactionIdentifier, player.getId());
    }
}
