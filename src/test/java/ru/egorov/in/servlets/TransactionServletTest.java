package ru.egorov.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egorov.in.dto.ExceptionResponse;
import ru.egorov.in.dto.SuccessResponse;
import ru.egorov.in.dto.TransactionHistoryResponse;
import ru.egorov.in.mappers.TransactionMapper;
import ru.egorov.in.security.Authentication;
import ru.egorov.model.Player;
import ru.egorov.service.PlayerService;
import ru.egorov.service.TransactionService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServletTest {

    private TransactionServlet servlet;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private ObjectMapper objectMapper;
    private TransactionMapper transactionMapper;
    private TransactionService transactionService;
    private PlayerService playerService;
    private ServletContext servletContext;
    private ServletInputStream servletInputStream;

    @BeforeEach
    void init() {
        response = mock(HttpServletResponse.class);
        request = mock(HttpServletRequest.class);
        objectMapper = new ObjectMapper();
        transactionMapper = mock(TransactionMapper.class);
        transactionService = mock(TransactionService.class);
        playerService = mock(PlayerService.class);
        servletContext = mock(ServletContext.class);

        servlet = new TransactionServlet() {
            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        String json = """
                {
                    "playerLogin": "test",
                    "amount": 0
                }""";


        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(json.getBytes());
        servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        servlet.setTransactionMapper(transactionMapper);
        servlet.setJacksonMapper(objectMapper);
        servlet.setTransactionService(transactionService);
        servlet.setPlayerService(playerService);
    }

    @Test
    void testDoGet_success() throws IOException, ServletException {
        Player player = new Player("test", "test", BigDecimal.ZERO);
        player.setId(1L);
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("test", true, null));
        when(request.getRequestURI()).thenReturn("/players/transactions/history");
        when(request.getParameter("login")).thenReturn("test");
        when(playerService.getByLogin("test")).thenReturn(player);
        when(transactionService.getPlayerHistory(player.getId())).thenReturn(Collections.emptyList());
        when(transactionMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());
        TransactionHistoryResponse history = new TransactionHistoryResponse("test", Collections.emptyList());

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        servlet.doGet(request, response);
        String json = objectMapper.writeValueAsString(history);

        assertEquals(json, stringWriter.toString());
    }

    @Test
    void testDoGet_failed() throws IOException, ServletException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("test", true, null));

        when(request.getRequestURI()).thenReturn("/players/transactions/history");
        when(request.getParameter("login")).thenReturn(null);
        ExceptionResponse exceptionResponse = new ExceptionResponse("Login parameter is null!");

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        servlet.doGet(request, response);
        String json = objectMapper.writeValueAsString(exceptionResponse);

        assertEquals(json, stringWriter.toString());
    }

    @Test
    void testDoPost_creditSuccess() throws IOException, ServletException {
        Player player = new Player("test", "test", BigDecimal.ZERO);
        player.setId(1L);
        when(request.getInputStream()).thenReturn(servletInputStream);
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("test", true, null));
        when(request.getRequestURI()).thenReturn("/players/transactions/credit");
        when(playerService.getByLogin("test")).thenReturn(player);

        when(transactionService.credit(eq(BigDecimal.valueOf(0)), any(UUID.class), eq(player.getId()))).thenReturn(true);

        SuccessResponse successResponse = new SuccessResponse("Transaction completed successfully!");

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        servlet.doPost(request, response);
        String json = objectMapper.writeValueAsString(successResponse);

        assertEquals(json, stringWriter.toString());

    }

    @Test
    void testDoPost_debitSuccess() throws IOException, ServletException {
        Player player = new Player("test", "test", BigDecimal.ZERO);
        player.setId(1L);
        when(request.getInputStream()).thenReturn(servletInputStream);
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("test", true, null));
        when(request.getRequestURI()).thenReturn("/players/transactions/debit");
        when(playerService.getByLogin("test")).thenReturn(player);

        when(transactionService.debit(eq(BigDecimal.valueOf(0)), any(UUID.class), eq(player.getId()))).thenReturn(true);

        SuccessResponse successResponse = new SuccessResponse("Transaction completed successfully!");

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        servlet.doPost(request, response);
        String json = objectMapper.writeValueAsString(successResponse);

        assertEquals(json, stringWriter.toString());
    }
}