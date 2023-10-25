package ru.egorov.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egorov.exception.AuthorizeException;
import ru.egorov.exception.RegisterException;
import ru.egorov.in.dto.ExceptionResponse;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.in.dto.SecurityDTO;
import ru.egorov.in.dto.SuccessResponse;
import ru.egorov.model.Player;
import ru.egorov.service.SecurityService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private SecurityService securityService;
    private ObjectMapper mapper;
    private ServletInputStream servletInputStream;
    private RegistrationServlet servlet;
    private SecurityDTO dto;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void beforeEach() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        securityService = mock(SecurityService.class);
        mapper = new ObjectMapper();

        String json = """
                {
                    "login": "test",
                    "password": "test"
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

        dto = new SecurityDTO("test", "test");
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        servlet = new RegistrationServlet();
        servlet.setJacksonMapper(mapper);
        servlet.setSecurityService(securityService);
    }
    @Test
    void testDoPostMethod_success() throws IOException, ServletException {
        when(request.getInputStream()).thenReturn(servletInputStream);

        when(response.getWriter()).thenReturn(writer);

        SuccessResponse successResponse = new SuccessResponse("Player with login " + dto.login() + " successfully created.");
        Player player = new Player(dto.login(), dto.password(), BigDecimal.ZERO);
        when(securityService.register(dto.login(), dto.password())).thenReturn(player);
        String jwtJson = mapper.writeValueAsString(successResponse);
        servlet.doPost(request, response);

        assertEquals(jwtJson, stringWriter.toString());
    }

    @Test
    void testDoPostMethod_failed() throws IOException, ServletException {
        when(request.getInputStream()).thenReturn(servletInputStream);

        when(response.getWriter()).thenReturn(writer);

        ExceptionResponse exceptionResponse = new ExceptionResponse("The player with this login already exists.");
        when(securityService.register(dto.login(), dto.password())).thenThrow(new RegisterException("The player with this login already exists."));
        String json = mapper.writeValueAsString(exceptionResponse);
        servlet.doPost(request, response);

        assertEquals(stringWriter.toString(), json);
    }
}