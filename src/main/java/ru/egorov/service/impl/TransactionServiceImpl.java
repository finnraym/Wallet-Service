package ru.egorov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.exception.TransactionAlreadyExistsException;
import ru.egorov.exception.TransactionOperationException;
import ru.egorov.model.Transaction;
import ru.egorov.repository.TransactionRepository;
import ru.egorov.service.PlayerService;
import ru.egorov.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The type Transaction service.
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionDAO;
    private final PlayerService playerService;

    @Transactional(readOnly = true)
    @Override
    public List<Transaction> getPlayerHistory(Long playerId) {
        return transactionDAO.findAllByPlayerId(playerId);
    }

    @Transactional
    @Override
    public void debit(BigDecimal amount, UUID transactionIdentifier, Long playerId) {
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
        playerService.updateBalance(playerId, result);
    }

    @Transactional
    @Override
    public void credit(BigDecimal amount, UUID transactionIdentifier, Long playerId) {
        checkTransaction(transactionIdentifier);
        Transaction transaction = openNewTransaction("credit", playerId);
        BigDecimal playerBalance = playerService.getPlayerBalance(playerId);

        transaction.setAmount(amount);
        transaction.setBalanceBefore(playerBalance);
        transaction.setTransactionIdentifier(transactionIdentifier);
        BigDecimal result = playerBalance.add(amount);
        transaction.setBalanceAfter(result);

        transactionDAO.save(transaction);
        playerService.updateBalance(playerId, result);
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
