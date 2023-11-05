package ru.egorov.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egorov.exception.TransactionAlreadyExistsException;
import ru.egorov.exception.TransactionOperationException;
import ru.egorov.model.Transaction;
import ru.egorov.repository.TransactionRepository;
import ru.egorov.service.PlayerService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * The type Transaction service impl test.
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private TransactionRepository transactionDAO;
    @Mock
    private PlayerService playerService;

    static private Transaction transaction1;
    static private UUID transaction1Identifier;
    static private Transaction transaction2;
    static private UUID transaction2Identifier;

    /**
     * Init.
     */
    @BeforeAll
    static void init() {
        transaction1Identifier = UUID.randomUUID();
        transaction2Identifier = UUID.randomUUID();
        transaction1 = new Transaction("credit", 1L, BigDecimal.valueOf(200), BigDecimal.valueOf(250), BigDecimal.valueOf(50), transaction1Identifier);
        transaction2 = new Transaction("debit", 1L, BigDecimal.valueOf(250), BigDecimal.valueOf(100), BigDecimal.valueOf(150), transaction2Identifier);
    }

    /**
     * Test get player history.
     */
    @Test
    void testGetPlayerHistory() {
        List<Transaction> transactions = List.of(
                transaction1, transaction2
        );

        Mockito.when(transactionDAO.findAllByPlayerId(1L)).thenReturn(transactions);
        List<Transaction> playerHistory = transactionService.getPlayerHistory(1L);
        assertIterableEquals(transactions, playerHistory);
    }

    /**
     * Test debit success.
     */
    @Test
    void testDebit_Success() {
        BigDecimal amount = BigDecimal.TEN;
        Long playerId = 1L;
        Mockito.when(transactionDAO.findByTransactionIdentifier(transaction1Identifier)).thenReturn(Optional.empty());
        Mockito.when(playerService.getPlayerBalance(1L)).thenReturn(BigDecimal.valueOf(200));

        transactionService.debit(amount, transaction1Identifier, playerId);
        Mockito.verify(transactionDAO, Mockito.times(1)).save(any());
        Mockito.verify(playerService, Mockito.times(1)).updateBalance(1L, BigDecimal.valueOf(190));
    }

    /**
     * Test debit failed check transaction.
     */
    @Test
    void testDebit_FailedCheckTransaction() {
        BigDecimal amount = BigDecimal.TEN;
        Long playerId = 1L;
        Mockito.when(transactionDAO.findByTransactionIdentifier(transaction1Identifier)).thenReturn(Optional.of(transaction1));

        assertThrows(TransactionAlreadyExistsException.class, () -> transactionService.debit(amount, transaction1Identifier, playerId));
    }

    /**
     * Test debit failed transaction operation.
     */
    @Test
    void testDebit_FailedTransactionOperation() {
        BigDecimal amount = BigDecimal.TEN;
        Long playerId = 1L;
        Mockito.when(transactionDAO.findByTransactionIdentifier(transaction1Identifier)).thenReturn(Optional.empty());
        Mockito.when(playerService.getPlayerBalance(1L)).thenReturn(BigDecimal.ONE);

        assertThrows(TransactionOperationException.class, () -> transactionService.debit(amount, transaction1Identifier, playerId));
    }

    /**
     * Test credit success.
     */
    @Test
    void testCredit_Success() {
        BigDecimal amount = BigDecimal.TEN;
        Long playerId = 1L;
        Mockito.when(transactionDAO.findByTransactionIdentifier(transaction1Identifier)).thenReturn(Optional.empty());
        Mockito.when(playerService.getPlayerBalance(1L)).thenReturn(BigDecimal.valueOf(200));

        transactionService.credit(amount, transaction1Identifier, playerId);
        Mockito.verify(transactionDAO, Mockito.times(1)).save(any());
        Mockito.verify(playerService, Mockito.times(1)).updateBalance(1L, BigDecimal.valueOf(210));
    }

    /**
     * Test credit failed check transaction.
     */
    @Test
    void testCredit_FailedCheckTransaction() {
        BigDecimal amount = BigDecimal.TEN;
        Long playerId = 1L;
        Mockito.when(transactionDAO.findByTransactionIdentifier(transaction1Identifier)).thenReturn(Optional.of(transaction1));

        assertThrows(TransactionAlreadyExistsException.class, () -> transactionService.credit(amount, transaction1Identifier, playerId));
    }

}