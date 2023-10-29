package ru.egorov.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.egorov.exception.AuthorizeException;
import ru.egorov.exception.PlayerNotFoundException;
import ru.egorov.exception.ValidationParametersException;
import ru.egorov.in.dto.ExceptionResponse;
import ru.egorov.in.mappers.PlayerMapper;
import ru.egorov.in.security.Authentication;
import ru.egorov.model.Player;
import ru.egorov.service.PlayerService;

import java.io.IOException;

@WebServlet("/players/balance")
public class ShowBalanceServlet extends HttpServlet {
    private PlayerService playerService;
    private ObjectMapper jacksonMapper;
    private PlayerMapper playerMapper;
    @Override
    public void init() throws ServletException {
        playerService = (PlayerService) getServletContext().getAttribute("playerService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
        playerMapper = (PlayerMapper) getServletContext().getAttribute("playerMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.isAuth()) {
            try {
                showBalanceProcess(req, resp, authentication);
            } catch (PlayerNotFoundException | ValidationParametersException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (AuthorizeException e) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(authentication.getMessage()));
        }
    }

    private void showBalanceProcess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws ValidationParametersException, IOException {
        String login = req.getParameter("login");
        if (login == null) throw new ValidationParametersException("Login parameter is null!");
        Player entity = playerService.getByLogin(login);
        if (!authentication.getLogin().equals(entity.getLogin())) throw new AuthorizeException("Incorrect credentials.");
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), playerMapper.toDto(entity));
    }

    public PlayerService getPlayerService() {
        return playerService;
    }

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public ObjectMapper getJacksonMapper() {
        return jacksonMapper;
    }

    public void setJacksonMapper(ObjectMapper jacksonMapper) {
        this.jacksonMapper = jacksonMapper;
    }

    public PlayerMapper getPlayerMapper() {
        return playerMapper;
    }

    public void setPlayerMapper(PlayerMapper playerMapper) {
        this.playerMapper = playerMapper;
    }
}
