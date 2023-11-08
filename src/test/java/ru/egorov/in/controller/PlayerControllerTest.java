package ru.egorov.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.egorov.in.dto.*;
import ru.egorov.in.mappers.PlayerMapper;
import ru.egorov.in.mappers.TransactionMapper;
import ru.egorov.model.Player;
import ru.egorov.service.PlayerService;
import ru.egorov.service.TransactionService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
@WithMockUser(username = "test",password = "test",roles = {})
class PlayerControllerTest {

//    @Autowired
    private MockMvc mvc;
    @MockBean
    private PlayerService playerService;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private PlayerMapper playerMapper;
    @MockBean
    private TransactionMapper transactionMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup()
    {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    void testGetBalance_shouldReturnPlayerDTO() throws Exception {
        final String login = "test";
        final String password = "test";
        final Player testPlayer = new Player(login, password, BigDecimal.ZERO);
        final PlayerDTO testPlayerDTO = new PlayerDTO(testPlayer.getLogin(), testPlayer.getBalance());
        when(playerService.getByLogin(login)).thenReturn(testPlayer);
        when(playerMapper.toDto(testPlayer)).thenReturn(testPlayerDTO);
        UserDetails userDetails = new User(login, password, Collections.emptyList());
        mvc.perform(get("/players/balance")
                        .with(user(userDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .param("login", login))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(testPlayerDTO)));

    }

    @WithMockUser(username = "test1",password = "test1",roles = {})
    @Test
    void testGetBalance_shouldThrowUnauthorizedException() throws Exception {
        final String login = "test";
        final String password = "test";
        final Player testPlayer = new Player(login, password, BigDecimal.ZERO);
        final PlayerDTO testPlayerDTO = new PlayerDTO(testPlayer.getLogin(), testPlayer.getBalance());
        when(playerService.getByLogin(login)).thenReturn(testPlayer);
        when(playerMapper.toDto(testPlayer)).thenReturn(testPlayerDTO);
        ExceptionResponse response = new ExceptionResponse("Incorrect login");
        mvc.perform(get("/players/balance")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("login", "test1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)));
    }

    @Test
    void testGetHistory_shouldReturnTransactionHistoryResponse() throws Exception {
        final String login = "test";
        final String password = "test";
        final Player testPlayer = new Player(login, password, BigDecimal.ZERO);
        testPlayer.setId(1L);
        when(playerService.getByLogin(login)).thenReturn(testPlayer);
        when(transactionService.getPlayerHistory(testPlayer.getId())).thenReturn(Collections.emptyList());
        when(transactionMapper.toDTOList(any(List.class))).thenReturn(Collections.emptyList());
        UserDetails userDetails = new User(login, password, Collections.emptyList());

        TransactionHistoryResponse response = new TransactionHistoryResponse(login, Collections.emptyList());
        mvc.perform(get("/players/history")
                        .with(user(userDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .param("login", login))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)));

    }

    @Test
    void testGetHistory_shouldThrowUnauthorizedException() throws Exception {
        final String login = "test";
        final String password = "test";
        final Player testPlayer = new Player(login, password, BigDecimal.ZERO);
        testPlayer.setId(1L);
        when(playerService.getByLogin(login)).thenReturn(testPlayer);
        when(transactionService.getPlayerHistory(testPlayer.getId())).thenReturn(Collections.emptyList());
        when(transactionMapper.toDTOList(any(List.class))).thenReturn(Collections.emptyList());
        ExceptionResponse response = new ExceptionResponse("Incorrect login");
        mvc.perform(get("/players/history")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("login", "test1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)));
    }

    @Test
    void testCredit_shouldReturnSuccessResponse() throws Exception {
        final String login = "test";
        final String password = "test";
        final Player testPlayer = new Player(login, password, BigDecimal.TEN);
        testPlayer.setId(1L);
        final BigDecimal amount = BigDecimal.TEN;
        final TransactionRequest request = new TransactionRequest(login, amount);
        when(playerService.getByLogin(login)).thenReturn(testPlayer);
        UserDetails userDetails = new User(login, password, Collections.emptyList());
        SuccessResponse response = new SuccessResponse("Transaction completed successfully!");
        mvc.perform(post("/players/transactions/credit")
                        .with(user(userDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)));

    }

    @Test
    void testDebit_shouldReturnSuccessResponse() throws Exception {
        final String login = "test";
        final String password = "test";
        final Player testPlayer = new Player(login, password, BigDecimal.TEN);
        testPlayer.setId(1L);
        final BigDecimal amount = BigDecimal.TEN;
        final TransactionRequest request = new TransactionRequest(login, amount);
        when(playerService.getByLogin(login)).thenReturn(testPlayer);
        UserDetails userDetails = new User(login, password, Collections.emptyList());
        SuccessResponse response = new SuccessResponse("Transaction completed successfully!");
        mvc.perform(post("/players/transactions/debit")
                        .with(user(userDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)));

    }
}