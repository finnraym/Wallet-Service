package ru.egorov.dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.egorov.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTransactionDAOTest {

    private InMemoryTransactionDAO transactionDAO;
    static private Transaction transaction1;
    static private UUID transaction1Identifier;
    static private Transaction transaction2;
    static private UUID transaction2Identifier;
    static private Transaction transaction3;
    static private UUID transaction3Identifier;

    @BeforeAll
    static void init() {
        transaction1Identifier = UUID.randomUUID();
        transaction2Identifier = UUID.randomUUID();
        transaction3Identifier = UUID.randomUUID();
        transaction1 = new Transaction("credit", 1L, BigDecimal.valueOf(200), BigDecimal.valueOf(250), BigDecimal.valueOf(50), transaction1Identifier);
        transaction2 = new Transaction("debit", 1L, BigDecimal.valueOf(250), BigDecimal.valueOf(100), BigDecimal.valueOf(150), transaction2Identifier);
        transaction3 = new Transaction("credit", 2L, BigDecimal.valueOf(50), BigDecimal.valueOf(12), BigDecimal.valueOf(62), transaction3Identifier);
    }

    @BeforeEach
    void refresh() {
        transactionDAO = new InMemoryTransactionDAO();
        transactionDAO.save(transaction1);
        transactionDAO.save(transaction2);
        transactionDAO.save(transaction3);
    }

    @AfterEach
    void tearDown() {
        transactionDAO = null;
    }
    @Test
    void testFindById_Success() {
        Optional<Transaction> byId = transactionDAO.findById(1L);
        Transaction transaction = byId.get();
        assertNotNull(transaction);
        assertEquals(transaction1.getPlayerId(), transaction.getPlayerId());
        assertEquals(transaction1.getType(), transaction.getType());
        assertEquals(transaction1.getBalanceBefore(), transaction.getBalanceBefore());
        assertEquals(transaction1.getBalanceAfter(), transaction.getBalanceAfter());
        assertEquals(transaction1.getAmount(), transaction.getAmount());
        assertEquals(transaction1.getTransactionIdentifier(), transaction.getTransactionIdentifier());
    }

    @Test
    void testFindById_Empty() {
        Optional<Transaction> byId = transactionDAO.findById(1L);
        Transaction transaction = byId.orElse(null);
        assertNull(transaction);
    }

    @Test
    void testFindAll() {
        List<Transaction> all = transactionDAO.findAll();
        assertEquals(3, all.size());
    }

    @Test
    void testFindAllByPlayerId_Success() {
        List<Transaction> allByPlayerId = transactionDAO.findAllByPlayerId(1L);
        assertEquals(2, allByPlayerId.size());
    }

    @Test
    void testFindByTransactionIdentifier_Success() {
        Optional<Transaction> byTransactionIdentifier = transactionDAO.findByTransactionIdentifier(transaction1Identifier);
        Transaction transaction = byTransactionIdentifier.orElse(null);
        assertNotNull(transaction);
        assertEquals(transaction1.getPlayerId(), transaction.getPlayerId());
        assertEquals(transaction1.getType(), transaction.getType());
        assertEquals(transaction1.getBalanceBefore(), transaction.getBalanceBefore());
        assertEquals(transaction1.getBalanceAfter(), transaction.getBalanceAfter());
        assertEquals(transaction1.getAmount(), transaction.getAmount());
        assertEquals(transaction1.getTransactionIdentifier(), transaction.getTransactionIdentifier());
    }

    @Test
    void testFindByTransactionIdentifier_Empty() {
        Optional<Transaction> byTransactionIdentifier = transactionDAO.findByTransactionIdentifier(transaction1Identifier);
        Transaction transaction = byTransactionIdentifier.orElse(null);
        assertNull(transaction);
    }

}