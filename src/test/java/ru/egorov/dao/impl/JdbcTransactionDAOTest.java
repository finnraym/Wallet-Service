package ru.egorov.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.egorov.config.DBConnectionProvider;
import ru.egorov.config.DBMigrationService;
import ru.egorov.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTransactionDAOTest extends JdbcAbstractDAOTest {

    private JdbcTransactionDAO transactionDAO;

    @BeforeEach
    void setUp() {
        DBConnectionProvider connectionProvider = new DBConnectionProvider(
                container.getJdbcUrl(), container.getUsername(), container.getPassword(),
                "org.postgresql.Driver");

        DBMigrationService migrationService = new DBMigrationService(connectionProvider, "migration", "db.changelog/changelog.xml");
        migrationService.migration();

        transactionDAO = new JdbcTransactionDAO(connectionProvider);
    }

    @Test
    void testFindById_success() {
        Optional<Transaction> optionalTransaction = transactionDAO.findById(1L);
        Transaction transaction = optionalTransaction.get();
        assertNotNull(transaction);

        Transaction expected = new Transaction(
                "credit", 2L,
                BigDecimal.valueOf(0.00), BigDecimal.valueOf(1500.00), BigDecimal.valueOf(1500.00),
                UUID.fromString("43611fa0-93d0-422f-b9d6-1a3b0147c601")
        );

        assertEquals(expected.getType(), transaction.getType());
        assertEquals(expected.getPlayerId(), transaction.getPlayerId());
        assertEquals(expected.getBalanceBefore().doubleValue(), transaction.getBalanceBefore().doubleValue());
        assertEquals(expected.getBalanceAfter().doubleValue(), transaction.getBalanceAfter().doubleValue());
        assertEquals(expected.getAmount().doubleValue(), transaction.getAmount().doubleValue());
        assertEquals(expected.getTransactionIdentifier(), transaction.getTransactionIdentifier());
    }

    @Test
    void testFindById_notFound() {
        Optional<Transaction> optionalTransaction = transactionDAO.findById(4L);
        assertTrue(optionalTransaction.isEmpty());
    }

    @Test
    void testFindAll() {
        List<Transaction> all = transactionDAO.findAll();
        assertEquals(3, all.size());
    }

    @Test
    void testSave_success() {
        UUID uuid = UUID.randomUUID();
        Transaction transaction = new Transaction(
                "credit", 3L, BigDecimal.valueOf(0), BigDecimal.valueOf(100), BigDecimal.valueOf(100), uuid
        );
        transactionDAO.save(transaction);
        Optional<Transaction> optionalTransaction = transactionDAO.findById(4L);
        assertNotNull(optionalTransaction.get());
    }

    @Test
    void testSave_failed() {
        UUID uuid = UUID.randomUUID();
        Transaction transaction = new Transaction(
                "credit", 3L, BigDecimal.valueOf(0), BigDecimal.valueOf(100), BigDecimal.valueOf(100), uuid
        );
        transaction.setPlayerId(null);
        assertThrows(RuntimeException.class, () -> transactionDAO.save(transaction));
    }

    @Test
    void testFindAllByPlayerId() {
        List<Transaction> transactions = transactionDAO.findAllByPlayerId(2L);
        assertEquals(2, transactions.size());
    }

    @Test
    void testFindByTransactionIdentifier_success() {
        Optional<Transaction> optionalTransaction = transactionDAO.findByTransactionIdentifier(UUID.fromString("43611fa0-93d0-422f-b9d6-1a3b0147c601"));
        Transaction transaction = optionalTransaction.get();
        assertNotNull(transaction);

        Transaction expected = new Transaction(
                "credit", 2L,
                BigDecimal.valueOf(0.00), BigDecimal.valueOf(1500.00), BigDecimal.valueOf(1500.00),
                UUID.fromString("43611fa0-93d0-422f-b9d6-1a3b0147c601")
        );

        assertEquals(expected.getType(), transaction.getType());
        assertEquals(expected.getPlayerId(), transaction.getPlayerId());
        assertEquals(expected.getBalanceBefore().doubleValue(), transaction.getBalanceBefore().doubleValue());
        assertEquals(expected.getBalanceAfter().doubleValue(), transaction.getBalanceAfter().doubleValue());
        assertEquals(expected.getAmount().doubleValue(), transaction.getAmount().doubleValue());
        assertEquals(expected.getTransactionIdentifier(), transaction.getTransactionIdentifier());
    }

    @Test
    void testFindByTransactionIdentifier_notFound() {
        Optional<Transaction> optionalTransaction = transactionDAO.findByTransactionIdentifier(UUID.fromString("43611fa0-93d0-422f-b9d6-1a5b0147c601"));
        assertTrue(optionalTransaction.isEmpty());
    }
}