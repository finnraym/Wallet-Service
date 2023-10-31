package ru.egorov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.egorov.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByPlayerId(Long playerId);

    Optional<Transaction> findByTransactionIdentifier(UUID transactionIdentifier);
}
