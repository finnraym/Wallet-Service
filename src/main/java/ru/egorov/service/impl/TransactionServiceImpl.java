package ru.egorov.service.impl;

import ru.egorov.dao.TransactionDAO;
import ru.egorov.exception.TransactionAlreadyExistsException;
import ru.egorov.exception.TransactionOperationException;
import ru.egorov.model.Transaction;
import ru.egorov.service.PlayerService;
import ru.egorov.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;
    private final PlayerService playerService;

    public TransactionServiceImpl(TransactionDAO transactionDAO, PlayerService playerService) {
        this.transactionDAO = transactionDAO;
        this.playerService = playerService;
    }

    @Override
    public List<Transaction> getPlayerHistory(Long playerId) {
        return transactionDAO.findAllByPlayerId(playerId);
    }

    @Override
    public boolean debit(BigDecimal amount, UUID transactionIdentifier, Long playerId) {
        checkTransaction(transactionIdentifier);
        Transaction transaction = openNewTransaction("debit", playerId);
        BigDecimal playerBalance = playerService.getPlayerBalance(playerId);

        if (playerBalance.compareTo(amount) < 0) {
            throw new TransactionOperationException("Недостаточно средств.");
        }

        BigDecimal result = playerBalance.subtract(amount);

        transactionDAO.save(transaction);
        return playerService.updateBalance(playerId, result);
    }

    @Override
    public boolean credit(BigDecimal amount, UUID transactionIdentifier, Long playerId) {
        checkTransaction(transactionIdentifier);
        Transaction transaction = openNewTransaction("credit", playerId);
        BigDecimal playerBalance = playerService.getPlayerBalance(playerId);

        BigDecimal result = playerBalance.add(amount);

        transactionDAO.save(transaction);
        return playerService.updateBalance(playerId, result);
    }

    private Transaction openNewTransaction(String type, Long playerId) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setPlayerId(playerId);
        return transaction;
    }

    private void checkTransaction(UUID transactionIdentifier) {
        if (transactionDAO.findByTransactionIdentifier(transactionIdentifier).isPresent()) {
            throw new TransactionAlreadyExistsException("Транзакция с идентификатором " + transactionIdentifier + " уже существует.");
        }
    }
}
