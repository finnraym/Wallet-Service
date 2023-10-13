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

/**
 * The type Transaction service.
 */
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;
    private final PlayerService playerService;

    /**
     * Instantiates a new Transaction service.
     *
     * @param transactionDAO the transaction dao
     * @param playerService  the player service
     */
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
            throw new TransactionOperationException("Insufficient funds.");
        }

        transaction.setAmount(amount);
        transaction.setBalanceBefore(playerBalance);
        transaction.setTransactionIdentifier(transactionIdentifier);
        BigDecimal result = playerBalance.subtract(amount);
        transaction.setBalanceAfter(result);

        transactionDAO.save(transaction);
        return playerService.updateBalance(playerId, result);
    }

    @Override
    public boolean credit(BigDecimal amount, UUID transactionIdentifier, Long playerId) {
        checkTransaction(transactionIdentifier);
        Transaction transaction = openNewTransaction("credit", playerId);
        BigDecimal playerBalance = playerService.getPlayerBalance(playerId);

        transaction.setAmount(amount);
        transaction.setBalanceBefore(playerBalance);
        transaction.setTransactionIdentifier(transactionIdentifier);
        BigDecimal result = playerBalance.add(amount);
        transaction.setBalanceAfter(result);

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
            throw new TransactionAlreadyExistsException("Transaction with ID " + transactionIdentifier + " already exists.");
        }
    }
}
