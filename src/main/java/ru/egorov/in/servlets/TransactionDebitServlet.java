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
import ru.egorov.exception.TransactionAlreadyExistsException;
import ru.egorov.exception.TransactionOperationException;
import ru.egorov.in.dto.*;
import ru.egorov.in.mappers.TransactionMapper;
import ru.egorov.model.Player;
import ru.egorov.model.Transaction;
import ru.egorov.service.PlayerService;
import ru.egorov.service.TransactionService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/players/transactions/debit")
public class TransactionDebitServlet extends HttpServlet {

    private PlayerService playerService;
    private TransactionService transactionService;
    private ObjectMapper jacksonMapper;
    @Override
    public void init() throws ServletException {
        playerService = (PlayerService) getServletContext().getAttribute("playerService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
        transactionService = (TransactionService) getServletContext().getAttribute("transactionService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        Player authPlayer = (Player) getServletContext().getAttribute("authentication");
        if (authPlayer != null) {
            try(ServletInputStream inputStream = req.getInputStream()) {
                TransactionRequest request = jacksonMapper.readValue(inputStream, TransactionRequest.class);
                Player entity = playerService.getByLogin(request.getPlayerLogin());
                if (!authPlayer.equals(entity)) throw new AuthorizeException("Incorrect credentials.");

                transactionService.debit(request.getAmount(), UUID.randomUUID(), entity.getId());

                resp.setStatus(HttpServletResponse.SC_OK);
                jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Debit transaction completed successfully!"));
            } catch (PlayerNotFoundException | TransactionAlreadyExistsException | TransactionOperationException e) {
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
