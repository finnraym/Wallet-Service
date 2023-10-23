package ru.egorov.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.egorov.exception.AuthorizeException;
import ru.egorov.in.dto.ExceptionResponse;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.in.dto.SecurityDTO;
import ru.egorov.service.SecurityService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private SecurityService securityService;
    private ObjectMapper jacksonMapper;
    @Override
    public void init() throws ServletException {
        securityService = (SecurityService) getServletContext().getAttribute("securityService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try(ServletInputStream inputStream = req.getInputStream()) {
            SecurityDTO securityDTO = jacksonMapper.readValue(inputStream, SecurityDTO.class);

            JwtResponse response = securityService.authorization(securityDTO.login(), securityDTO.password());

            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            jacksonMapper.writeValue(resp.getWriter(), response);
        } catch (AuthorizeException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            jacksonMapper.writeValue(resp.getWriter(), exceptionResponse);
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            jacksonMapper.writeValue(resp.getWriter(), exceptionResponse);
        }
    }
}
