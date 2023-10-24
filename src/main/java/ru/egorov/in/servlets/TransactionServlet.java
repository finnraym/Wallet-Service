package ru.egorov.in.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.egorov.exception.*;
import ru.egorov.in.dto.ExceptionResponse;
import ru.egorov.in.dto.SuccessResponse;
import ru.egorov.in.dto.TransactionHistoryResponse;
import ru.egorov.in.dto.TransactionRequest;
import ru.egorov.in.mappers.TransactionMapper;
import ru.egorov.in.security.Authentication;
import ru.egorov.model.Player;
import ru.egorov.model.Transaction;
import ru.egorov.service.PlayerService;
import ru.egorov.service.TransactionService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/players/transactions/*")
public class TransactionServlet extends HttpServlet {

    private PlayerService playerService;
    private TransactionService transactionService;
    private ObjectMapper jacksonMapper;
    private TransactionMapper transactionMapper;
    @Override
    public void init() throws ServletException {
        playerService = (PlayerService) getServletContext().getAttribute("playerService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
        transactionService = (TransactionService) getServletContext().getAttribute("transactionService");
        transactionMapper = (TransactionMapper) getServletContext().getAttribute("transactionMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.isAuth()) {
            try {
                if (req.getRequestURI().endsWith("/history")) {
                    transactionHistoryProcess(req, resp, authentication);
                }
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.isAuth()) {
            try(ServletInputStream inputStream = req.getInputStream()) {
                TransactionRequest request = jacksonMapper.readValue(inputStream, TransactionRequest.class);

                requestValidation(request);

                Player entity = playerService.getByLogin(request.getPlayerLogin());
                if (!authentication.getLogin().equals(entity.getLogin())) throw new AuthorizeException("Incorrect credentials.");


                String requestURI = req.getRequestURI();
                if (requestURI.endsWith("/credit")) {
                    transactionService.credit(request.getAmount(), UUID.randomUUID(), entity.getId());
                } else if (requestURI.endsWith("/debit")) {
                    transactionService.debit(request.getAmount(), UUID.randomUUID(), entity.getId());
                }

                resp.setStatus(HttpServletResponse.SC_OK);
                jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Transaction completed successfully!"));
            } catch (PlayerNotFoundException | TransactionAlreadyExistsException | TransactionOperationException | JsonParseException e) {
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

    private void requestValidation(TransactionRequest request) throws ValidationParametersException {
        if (request.getPlayerLogin()==null || request.getPlayerLogin().isBlank()) {
            throw new ValidationParametersException("Player login must not be null or empty.");
        } else if (request.getAmount().signum() == -1) {
            throw new ValidationParametersException("Transaction's amount must not be negative.");
        }
    }

    private void transactionHistoryProcess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws ValidationParametersException, IOException {
        String login = req.getParameter("login");
        if (login == null) throw new ValidationParametersException("Login parameter is null!");
        Player entity = playerService.getByLogin(login);
        if (!authentication.getLogin().equals(entity.getLogin())) throw new AuthorizeException("Incorrect credentials.");
        List<Transaction> playerHistory = transactionService.getPlayerHistory(entity.getId());
        TransactionHistoryResponse response = new TransactionHistoryResponse(entity.getLogin(), transactionMapper.toDTOList(playerHistory));
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), response);
    }
}
