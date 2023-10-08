package ru.egorov.dao;

import ru.egorov.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionDAO extends GeneralDAO<Long, Transaction>{
    List<Transaction> findAllByPlayerId(Long playerId);

    Optional<Transaction> findByTransactionIdentifier(UUID transactionIdentifier);
}
