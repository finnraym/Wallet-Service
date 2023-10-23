package ru.egorov.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.egorov.exception.AuthorizeException;
import ru.egorov.exception.PlayerNotFoundException;
import ru.egorov.in.dto.ExceptionResponse;
import ru.egorov.in.dto.PlayerDTO;
import ru.egorov.in.mappers.PlayerMapper;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        Player authPlayer = (Player) getServletContext().getAttribute("authentication");
        if (authPlayer != null) {
            try(ServletInputStream inputStream = req.getInputStream()) {
                PlayerDTO playerDTO = jacksonMapper.readValue(inputStream, PlayerDTO.class);
                Player entity = playerService.getByLogin(playerDTO.getLogin());
                if (!authPlayer.equals(entity)) throw new AuthorizeException("Incorrect credentials.");
                resp.setStatus(HttpServletResponse.SC_OK);
                jacksonMapper.writeValue(resp.getWriter(), playerMapper.toDto(entity));
            } catch (PlayerNotFoundException e) {
                ExceptionResponse response = new ExceptionResponse(e.getMessage());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jacksonMapper.writeValue(resp.getWriter(), response);
            } catch (AuthorizeException e) {
                ExceptionResponse response = new ExceptionResponse(e.getMessage());
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jacksonMapper.writeValue(resp.getWriter(), response);
            } catch (RuntimeException e) {
                ExceptionResponse response = new ExceptionResponse(e.getMessage());
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jacksonMapper.writeValue(resp.getWriter(), response);
            }
        } else {
            ExceptionResponse response = new ExceptionResponse("Access denied!");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jacksonMapper.writeValue(resp.getWriter(), response);
        }
    }
}
