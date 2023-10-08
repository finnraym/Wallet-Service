package ru.egorov.service;

import ru.egorov.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<Transaction> getPlayerHistory(Long playerId);
    boolean debit(BigDecimal amount, UUID transactionIdentifier, Long playerId);
    boolean credit(BigDecimal amount, UUID transactionIdentifier, Long playerId);

}
