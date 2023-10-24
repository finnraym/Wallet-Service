package ru.egorov.service;

import ru.egorov.aop.annotations.Loggable;
import ru.egorov.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The interface Transaction service.
 */
public interface TransactionService {
    /**
     * Gets player history.
     *
     * @param playerId the player id
     * @return the player history
     */
    List<Transaction> getPlayerHistory(Long playerId);

    /**
     * Debit boolean.
     *
     * @param amount                the amount
     * @param transactionIdentifier the transaction identifier
     * @param playerId              the player id
     * @return the boolean
     */
    boolean debit(BigDecimal amount, UUID transactionIdentifier, Long playerId);

    /**
     * Credit boolean.
     *
     * @param amount                the amount
     * @param transactionIdentifier the transaction identifier
     * @param playerId              the player id
     * @return the boolean
     */
    boolean credit(BigDecimal amount, UUID transactionIdentifier, Long playerId);

}
