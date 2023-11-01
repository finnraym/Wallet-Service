package ru.egorov.in.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
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

import javax.validation.Valid;

/**
 * The security controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Validated
@Api(value = "SecurityController" , tags = {"Security Controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Security Controller")
})
public class SecurityController {

    private final SecurityService securityService;
    private final PlayerMapper playerMapper;

    /**
     * Authorization player in application
     *
     * @param dto the security request
     * @return response entity
     */
    @ApiOperation(value = "Return the JWT", response = JwtResponse.class, tags = "login")
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
    @ApiOperation(value = "Return the player dto", response = PlayerDTO.class, tags = "registration")
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody SecurityRequest dto) {
        Player register = securityService.register(dto.login(), dto.password());
        return ResponseEntity.ok(playerMapper.toDto(register));
    }
}
