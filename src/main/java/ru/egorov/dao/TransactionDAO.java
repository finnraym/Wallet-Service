package ru.egorov.dao;

import ru.egorov.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Transaction dao.
 */
public interface TransactionDAO extends GeneralDAO<Long, Transaction>{
    /**
     * Find all by player id list.
     *
     * @param playerId the player id
     * @return the list
     */
    List<Transaction> findAllByPlayerId(Long playerId);

    /**
     * Find by transaction identifier optional.
     *
     * @param transactionIdentifier the transaction identifier
     * @return the optional
     */
    Optional<Transaction> findByTransactionIdentifier(UUID transactionIdentifier);
}
