package ru.egorov.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.egorov.exception.AuthorizeException;
import ru.egorov.in.dto.*;
import ru.egorov.in.mappers.PlayerMapper;
import ru.egorov.in.mappers.TransactionMapper;
import ru.egorov.model.Player;
import ru.egorov.service.PlayerService;
import ru.egorov.service.TransactionService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * controller for working with the player and his data
 */
@RestController
@RequestMapping("/players")
@Validated
@Tag(name = "Player controller", description = "Player API")
public class PlayerController {

    private final PlayerService playerService;
    private final TransactionService transactionService;
    private final PlayerMapper playerMapper;
    private final TransactionMapper transactionMapper;
    private SecurityContext securityContext;

    @Autowired
    public PlayerController(PlayerService playerService, TransactionService transactionService, PlayerMapper playerMapper, TransactionMapper transactionMapper) {
        this.playerService = playerService;
        this.transactionService = transactionService;
        this.playerMapper = playerMapper;
        this.transactionMapper = transactionMapper;
        this.securityContext = SecurityContextHolder.getContext();
    }

    public PlayerController(PlayerService playerService, TransactionService transactionService, PlayerMapper playerMapper, TransactionMapper transactionMapper, SecurityContext securityContext) {
        this.playerService = playerService;
        this.transactionService = transactionService;
        this.playerMapper = playerMapper;
        this.transactionMapper = transactionMapper;
        this.securityContext = securityContext;
    }

    /**
     * Get player's balance.
     *
     * @param login the player login
     * @return response entity
     */
    @Operation(summary = "Get player's balance by player login")
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestParam String login) {
        if (!isValidLogin(login)) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login"));
        Player player = playerService.getByLogin(login);
        return ResponseEntity.ok(playerMapper.toDto(player));
    }

    /**
     * Get the player's transactions history
     *
     * @param login the player login
     * @return response entity
     */
    @Operation(summary = "Get player's transaction history by player login")
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam String login) {
        if (!isValidLogin(login)) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login"));
        Player player = playerService.getByLogin(login);
        List<TransactionResponse> list = transactionMapper.toDTOList(
                transactionService.getPlayerHistory(player.getId()));
        return ResponseEntity.ok().body(new TransactionHistoryResponse(login, list));
    }

    /**
     * Credit transaction process
     *
     * @param request the transaction request
     * @return response entity
     */
    @Operation(summary = "Credit transaction process")
    @PostMapping("/transactions/credit")
    public ResponseEntity<?> credit(@RequestBody @Valid TransactionRequest request) {
        if (!isValidLogin(request.getPlayerLogin())) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login"));
        Player player = playerService.getByLogin(request.getPlayerLogin());
        transactionService.credit(request.getAmount(), UUID.randomUUID(), player.getId());
        return ResponseEntity.ok(new SuccessResponse("Transaction completed successfully!"));

    }

    /**
     * Debit transaction process
     *
     * @param request the transaction request
     * @return response entity
     */
    @Operation(summary = "Debit transaction process")
    @PostMapping("/transactions/debit")
    public ResponseEntity<?> debit(@RequestBody @Valid TransactionRequest request) {
        if (!isValidLogin(request.getPlayerLogin())) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login"));
        Player player = playerService.getByLogin(request.getPlayerLogin());
        transactionService.debit(request.getAmount(), UUID.randomUUID(), player.getId());
        return ResponseEntity.ok(new SuccessResponse("Transaction completed successfully!"));
    }

    private boolean isValidLogin(String login) {
        if (securityContext.getAuthentication() == null) securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) throw new AuthorizeException("Unauthorized!");
        User principal = (User) authentication.getPrincipal();
        return principal.getUsername().equals(login);
    }
}
