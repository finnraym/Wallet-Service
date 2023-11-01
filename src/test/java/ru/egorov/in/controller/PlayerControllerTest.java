package ru.egorov.in.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import ru.egorov.exception.AuthorizeException;
import ru.egorov.in.dto.PlayerDTO;
import ru.egorov.in.dto.SuccessResponse;
import ru.egorov.in.dto.TransactionHistoryResponse;
import ru.egorov.in.dto.TransactionRequest;
import ru.egorov.in.mappers.PlayerMapper;
import ru.egorov.in.mappers.TransactionMapper;
import ru.egorov.model.Player;
import ru.egorov.service.PlayerService;
import ru.egorov.service.TransactionService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class PlayerControllerTest {
    @Mock
    private PlayerService playerService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private PlayerMapper playerMapper;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        playerController.setSecurityContext(securityContext);
    }

    @Test
    void testGetBalance_shouldReturnPlayerDTO() throws Exception {
        final String login = "test";
        final String password = "test";
        final Player testPlayer = new Player(login, password, BigDecimal.ZERO);
        final PlayerDTO testPlayerDTO = new PlayerDTO(testPlayer.getLogin(), testPlayer.getBalance());
        when(playerService.getByLogin(login)).thenReturn(testPlayer);
        when(playerMapper.toDto(testPlayer)).thenReturn(testPlayerDTO);
        Authentication authentication = new UsernamePasswordAuthenticationToken(new User(login, password, Collections.emptyList()), password);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        ResponseEntity<?> response = playerController.getBalance(login);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        PlayerDTO body = (PlayerDTO) response.getBody();

        assertNotNull(body);
        assertEquals(testPlayerDTO.getLogin(), body.getLogin());
        assertEquals(testPlayerDTO.getBalance().doubleValue(), body.getBalance().doubleValue());
    }

    @Test
    void testGetBalance_shouldThrowUnauthorizedException() throws Exception {
        final String login = "test";
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(AuthorizeException.class, () -> playerController.getBalance(login));
    }

    @Test
    void testGetHistory_shouldReturnTransactionHistoryResponse() {
        final String login = "test";
        final String password = "test";
        final Player testPlayer = new Player(login, password, BigDecimal.ZERO);
        testPlayer.setId(1L);
        final PlayerDTO testPlayerDTO = new PlayerDTO(testPlayer.getLogin(), testPlayer.getBalance());
        when(playerService.getByLogin(login)).thenReturn(testPlayer);
        when(transactionService.getPlayerHistory(testPlayer.getId())).thenReturn(Collections.emptyList());
        when(transactionMapper.toDTOList(any(List.class))).thenReturn(Collections.emptyList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(new User(login, password, Collections.emptyList()), password);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        ResponseEntity<?> response = playerController.getHistory(login);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        TransactionHistoryResponse body = (TransactionHistoryResponse) response.getBody();

        assertNotNull(body);
        assertEquals(testPlayerDTO.getLogin(), body.playerLogin());
        assertEquals(0, body.transactions().size());
    }

    @Test
    void testGetHistory_shouldThrowUnauthorizedException() throws Exception {
        final String login = "test";
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(AuthorizeException.class, () -> playerController.getHistory(login));
    }

    @Test
    void testCredit_shouldReturnSuccessResponse() {
        final String login = "test";
        final String password = "test";
        final Player testPlayer = new Player(login, password, BigDecimal.ZERO);
        testPlayer.setId(1L);
        final BigDecimal amount = BigDecimal.ZERO;
        final TransactionRequest request = new TransactionRequest(login, amount);
        when(playerService.getByLogin(login)).thenReturn(testPlayer);
        Authentication authentication = new UsernamePasswordAuthenticationToken(new User(login, password, Collections.emptyList()), password);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        ResponseEntity<?> response = playerController.credit(request);
        verify(transactionService, times(1)).credit(eq(amount), any(UUID.class), eq(testPlayer.getId()));

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        SuccessResponse body = (SuccessResponse) response.getBody();

        assertNotNull(body);
        assertEquals("Transaction completed successfully!", body.message());
    }

    @Test
    void testDebit_shouldReturnSuccessResponse() {
        final String login = "test";
        final String password = "test";
        final Player testPlayer = new Player(login, password, BigDecimal.ZERO);
        testPlayer.setId(1L);
        final BigDecimal amount = BigDecimal.ZERO;
        final TransactionRequest request = new TransactionRequest(login, amount);
        when(playerService.getByLogin(login)).thenReturn(testPlayer);
        Authentication authentication = new UsernamePasswordAuthenticationToken(new User(login, password, Collections.emptyList()), password);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        ResponseEntity<?> response = playerController.debit(request);
        verify(transactionService, times(1)).debit(eq(amount), any(UUID.class), eq(testPlayer.getId()));

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        SuccessResponse body = (SuccessResponse) response.getBody();

        assertNotNull(body);
        assertEquals("Transaction completed successfully!", body.message());
    }
}