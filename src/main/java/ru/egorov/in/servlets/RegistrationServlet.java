package ru.egorov.in.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.egorov.exception.RegisterException;
import ru.egorov.in.dto.ExceptionResponse;
import ru.egorov.in.dto.SecurityDTO;
import ru.egorov.in.dto.SuccessResponse;
import ru.egorov.model.Player;
import ru.egorov.service.SecurityService;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private SecurityService securityService;
    private ObjectMapper jacksonMapper;
    @Override
    public void init() throws ServletException {
        securityService = (SecurityService) getServletContext().getAttribute("securityService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try(ServletInputStream inputStream = req.getInputStream()) {
            SecurityDTO securityDTO = jacksonMapper.readValue(inputStream, SecurityDTO.class);
            Player registered = securityService.register(securityDTO.login(), securityDTO.password());

            resp.setStatus(HttpServletResponse.SC_CREATED);
            jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Player with login " + registered.getLogin() + " successfully created."));
        } catch (RegisterException | JsonParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    public SecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public ObjectMapper getJacksonMapper() {
        return jacksonMapper;
    }

    public void setJacksonMapper(ObjectMapper jacksonMapper) {
        this.jacksonMapper = jacksonMapper;
    }
}
