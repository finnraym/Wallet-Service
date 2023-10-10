package ru.egorov.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egorov.exception.NotValidArgumentException;
import ru.egorov.model.Player;
import ru.egorov.service.PlayerService;
import ru.egorov.service.SecurityService;
import ru.egorov.service.TransactionService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * The type Main controller test.
 */
@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    @InjectMocks
    private MainController mainController;

    @Mock
    private TransactionService transactionService;
    @Mock
    private SecurityService securityService;
    @Mock
    private PlayerService playerService;

    /**
     * Test register success.
     */
    @Test
    void testRegister_Success() {
        String login = "login";
        String password = "password";

        Player expected = new Player(login, password, BigDecimal.ZERO);
        Mockito.when(securityService.register(login, password)).thenReturn(expected);

        Player result = mainController.register(login, password);

        assertEquals(expected, result);
    }

    /**
     * Test register invalid arguments.
     */
    @Test
    void testRegister_InvalidArguments() {
        String login = "";
        String password = "password";

        assertThrows(NotValidArgumentException.class, () -> mainController.register(login, password));
    }

    /**
     * Test authorize success.
     */
    @Test
    void testAuthorize_Success() {
        String login = "login";
        String password = "password";

        Player expected = new Player(login, password, BigDecimal.ZERO);
        Mockito.when(securityService.authorization(login, password)).thenReturn(expected);

        Player result = mainController.authorize(login, password);

        assertEquals(expected, result);
    }

    /**
     * Test show balance success.
     */
    @Test
    void testShowBalance_Success() {
        mainController.showBalance(new Player());
        Mockito.verify(playerService, Mockito.times(1)).getPlayerBalance(any());
    }

    /**
     * Test show transactions history success.
     */
    @Test
    void testShowTransactionsHistory_Success() {
        mainController.showTransactionsHistory(new Player());
        Mockito.verify(transactionService, Mockito.times(1)).getPlayerHistory(any());
    }

    /**
     * Test debit transaction success.
     */
    @Test
    void testDebitTransaction_Success() {
        UUID uuid = UUID.randomUUID();
        Player player = new Player();
        player.setId(1L);
        mainController.debitTransaction(BigDecimal.ONE, uuid, player);
        Mockito.verify(transactionService, Mockito.times(1)).debit(BigDecimal.ONE, uuid, player.getId());
    }

    /**
     * Test debit transaction invalid argument.
     */
    @Test
    void testDebitTransaction_InvalidArgument() {
        UUID uuid = UUID.randomUUID();
        Player player = new Player();
        assertThrows(NotValidArgumentException.class, () -> mainController.debitTransaction(BigDecimal.valueOf(-10), uuid, player));
    }

    /**
     * Test credit transaction success.
     */
    @Test
    void testCreditTransaction_Success() {
        UUID uuid = UUID.randomUUID();
        Player player = new Player();
        player.setId(1L);
        mainController.creditTransaction(BigDecimal.ONE, uuid, player);
        Mockito.verify(transactionService, Mockito.times(1)).credit(BigDecimal.ONE, uuid, player.getId());
    }

    /**
     * Test credit transaction invalid argument.
     */
    @Test
    void testCreditTransaction_InvalidArgument() {
        UUID uuid = UUID.randomUUID();
        Player player = new Player();
        assertThrows(NotValidArgumentException.class, () -> mainController.creditTransaction(BigDecimal.valueOf(-10), uuid, player));
    }
}