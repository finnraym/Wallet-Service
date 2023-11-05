package ru.egorov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.egorov.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The transaction repository
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find all transactions by player id
     *
     * @param playerId the player id
     * @return the player's list of transactions
     */
    List<Transaction> findAllByPlayerId(Long playerId);

    /**
     * Find transaction by transaction identifier
     *
     * @param transactionIdentifier the transaction identifier
     * @return optional of transaction
     */
    Optional<Transaction> findByTransactionIdentifier(UUID transactionIdentifier);
}
