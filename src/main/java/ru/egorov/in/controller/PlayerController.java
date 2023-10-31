package ru.egorov.in.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
@Validated
public class PlayerController {

    private final PlayerService playerService;
    private final TransactionService transactionService;
    private final PlayerMapper playerMapper;
    private final TransactionMapper transactionMapper;

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestParam String login) {
        if (!isValidLogin(login)) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login"));
        Player player = playerService.getByLogin(login);
        return ResponseEntity.ok(playerMapper.toDto(player));
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam String login) {
        if (!isValidLogin(login)) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login"));
        Player player = playerService.getByLogin(login);
        List<TransactionResponse> list = transactionMapper.toDTOList(
                transactionService.getPlayerHistory(player.getId()));
        return ResponseEntity.ok().body(new TransactionHistoryResponse(login, list));
    }

    @PostMapping("/transactions/credit")
    public ResponseEntity<?> credit(@RequestBody @Valid TransactionRequest request) {
        if (!isValidLogin(request.getPlayerLogin())) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login"));
        Player player = playerService.getByLogin(request.getPlayerLogin());
        transactionService.credit(request.getAmount(), UUID.randomUUID(), player.getId());
        return ResponseEntity.ok(new SuccessResponse("Transaction completed successfully!"));

    }

    @PostMapping("/transactions/debit")
    public ResponseEntity<?> debit(@RequestBody @Valid TransactionRequest request) {
        if (!isValidLogin(request.getPlayerLogin())) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login"));
        Player player = playerService.getByLogin(request.getPlayerLogin());
        transactionService.debit(request.getAmount(), UUID.randomUUID(), player.getId());
        return ResponseEntity.ok(new SuccessResponse("Transaction completed successfully!"));
    }

    private boolean isValidLogin(String login) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) throw new AuthorizeException("Unauthorized!");
        User principal = (User) authentication.getPrincipal();
        return principal.getUsername().equals(login);
    }
}
