package ru.egorov.dao.impl;

import ru.egorov.dao.TransactionDAO;
import ru.egorov.model.Transaction;

import java.util.*;

public class InMemoryTransactionDAO implements TransactionDAO {
    private final Map<Long, Transaction> transactions = new HashMap<>();
    private Long id = 1L;
    @Override
    public Optional<Transaction> findById(Long id) {
        Transaction transaction = transactions.get(id);
        return transaction == null ? Optional.empty() : Optional.of(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions.values());
    }

    @Override
    public List<Transaction> findAllByPlayerId(Long playerId) {
        List<Transaction> result = new ArrayList<>();

        for (Transaction transaction : transactions.values()) {
            if (transaction.getPlayerId().equals(playerId)) {
                result.add(transaction);
            }
        }

        return result;
    }

    @Override
    public Optional<Transaction> findByTransactionIdentifier(UUID transactionIdentifier) {
        Optional<Transaction> optional = Optional.empty();

        for (Transaction transaction : transactions.values()) {
            if (transaction.getTransactionIdentifier().equals(transactionIdentifier)) {
                optional = Optional.of(transaction);
                break;
            }
        }
        return optional;
    }

    @Override
    public Transaction save(Transaction transaction) {
        transaction.setId(getLastId());
        incrementId();
        transactions.put(transaction.getId(), transaction);
        return transactions.get(transaction.getId());
    }

    private Long getLastId() {
        return id;
    }
    private void incrementId() {
        id++;
    }
}
