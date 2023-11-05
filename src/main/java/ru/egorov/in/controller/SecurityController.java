package ru.egorov.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.in.dto.PlayerDTO;
import ru.egorov.in.dto.SecurityRequest;
import ru.egorov.in.mappers.PlayerMapper;
import ru.egorov.model.Player;
import ru.egorov.service.SecurityService;

import jakarta.validation.Valid;

/**
 * The security controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth controller", description = "Auth API")
@Validated
public class SecurityController {

    private final SecurityService securityService;
    private final PlayerMapper playerMapper;

    /**
     * Authorization player in application
     *
     * @param dto the security request
     * @return response entity
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody SecurityRequest dto) {
        JwtResponse response = securityService.authorization(dto.login(), dto.password());
        return ResponseEntity.ok(response);
    }

    /**
     * Register the player in application
     *
     * @param dto the security request
     * @return response entity
     */
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody SecurityRequest dto) {
        Player register = securityService.register(dto.login(), dto.password());
        return ResponseEntity.ok(playerMapper.toDto(register));
    }
}
