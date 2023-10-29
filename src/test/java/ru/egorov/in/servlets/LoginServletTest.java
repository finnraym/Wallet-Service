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
import ru.egorov.in.dto.ExceptionResponse;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.in.dto.SecurityDTO;
import ru.egorov.service.SecurityService;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private SecurityService securityService;
    private ObjectMapper mapper;
    private ServletInputStream servletInputStream;
    private LoginServlet servlet;
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

        servlet = new LoginServlet();
        servlet.setJacksonMapper(mapper);
        servlet.setSecurityService(securityService);
    }

    @Test
    void testDoPostMethod_success() throws IOException, ServletException {
        when(request.getInputStream()).thenReturn(servletInputStream);

        when(response.getWriter()).thenReturn(writer);

        JwtResponse jwt = new JwtResponse("test", "accessToken", "refreshToken");
        when(securityService.authorization(dto.login(), dto.password())).thenReturn(jwt);
        String jwtJson = mapper.writeValueAsString(jwt);
        servlet.doPost(request, response);

        assertEquals(stringWriter.toString(), jwtJson);
    }

    @Test
    void testDoPostMethod_failed() throws IOException, ServletException {
        when(request.getInputStream()).thenReturn(servletInputStream);

        when(response.getWriter()).thenReturn(writer);

        ExceptionResponse exceptionResponse = new ExceptionResponse("Incorrect password.");
        when(securityService.authorization(dto.login(), dto.password())).thenThrow(new AuthorizeException("Incorrect password."));
        String json = mapper.writeValueAsString(exceptionResponse);
        servlet.doPost(request, response);

        assertEquals(stringWriter.toString(), json);
    }
}