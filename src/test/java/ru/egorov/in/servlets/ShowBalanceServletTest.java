package ru.egorov.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egorov.in.dto.ExceptionResponse;
import ru.egorov.in.dto.PlayerDTO;
import ru.egorov.in.mappers.PlayerMapper;
import ru.egorov.in.security.Authentication;
import ru.egorov.model.Player;
import ru.egorov.service.PlayerService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowBalanceServletTest {

    private ShowBalanceServlet servlet;
    private PlayerService playerService;
    private PlayerMapper playerMapper;
    private ObjectMapper objectMapper;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;

    @BeforeEach
    void beforeEach() {
        playerMapper = mock(PlayerMapper.class);
        playerService = mock(PlayerService.class);
        objectMapper = new ObjectMapper();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servletContext = mock(ServletContext.class);
        servlet = new ShowBalanceServlet() {
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        servlet.setJacksonMapper(objectMapper);
        servlet.setPlayerMapper(playerMapper);
        servlet.setPlayerService(playerService);
    }

    @Test
    void testDoGet_success() throws IOException, ServletException {
        Player player = new Player("test", "test", BigDecimal.ZERO);
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("test", true, null));

        when(request.getParameter("login")).thenReturn("test");
        when(playerService.getByLogin("test")).thenReturn(player);
        PlayerDTO dto = new PlayerDTO(player.getLogin(), player.getBalance());
        when(playerMapper.toDto(player)).thenReturn(dto);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        servlet.doGet(request, response);
        String json = objectMapper.writeValueAsString(dto);

        assertEquals(json, stringWriter.toString());
    }

    @Test
    void testDoGet_failed() throws IOException, ServletException {
        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("test", true, null));

        when(request.getParameter("login")).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        ExceptionResponse exceptionResponse = new ExceptionResponse("Login parameter is null!");

        servlet.doGet(request, response);
        String json = objectMapper.writeValueAsString(exceptionResponse);

        assertEquals(json, stringWriter.toString());
    }
}