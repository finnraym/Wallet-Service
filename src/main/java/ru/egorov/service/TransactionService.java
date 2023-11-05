package ru.egorov.service;

import ru.egorov.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The interface security service.
 */
public interface TransactionService {


    /**
     * Get the player's transactions history
     *
     * @param playerId the player id
     * @return the player's list of transactions
     */
    List<Transaction> getPlayerHistory(Long playerId);

    /**
     * Debit transaction process
     *
     * @param amount the amount of transaction
     * @param transactionIdentifier the transaction identifier
     * @param playerId the player id
     */
    void debit(BigDecimal amount, UUID transactionIdentifier, Long playerId);

    /**
     * Credit transaction process
     *
     * @param amount the amount of transaction
     * @param transactionIdentifier the transaction identifier
     * @param playerId the player id
     */
    void credit(BigDecimal amount, UUID transactionIdentifier, Long playerId);

}
