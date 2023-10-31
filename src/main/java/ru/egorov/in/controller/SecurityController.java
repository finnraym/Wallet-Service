package ru.egorov.in.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.in.dto.SecurityRequest;
import ru.egorov.in.mappers.PlayerMapper;
import ru.egorov.model.Player;
import ru.egorov.service.SecurityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Validated
public class SecurityController {

    private final SecurityService securityService;
    private final PlayerMapper playerMapper;
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody SecurityRequest dto) {
        JwtResponse response = securityService.authorization(dto.login(), dto.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody SecurityRequest dto) {
        Player register = securityService.register(dto.login(), dto.password());
        return ResponseEntity.ok(playerMapper.toDto(register));
    }
}
