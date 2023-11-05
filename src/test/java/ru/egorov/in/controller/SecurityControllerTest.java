package ru.egorov.in.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.in.dto.PlayerDTO;
import ru.egorov.in.dto.SecurityRequest;
import ru.egorov.in.mappers.PlayerMapper;
import ru.egorov.model.Player;
import ru.egorov.service.SecurityService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class SecurityControllerTest {

    @Mock
    private SecurityService securityService;
    @Mock
    private PlayerMapper playerMapper;
    @InjectMocks
    private SecurityController securityController;
    @Test
    void testLogin_shouldReturnJwtResponse() {
        final String login = "test";
        final String password = "test";
        final SecurityRequest request = new SecurityRequest(login, password);
        JwtResponse jwt = new JwtResponse(login, "accessToken", "refreshToken");
        when(securityService.authorization(request.login(), request.password())).thenReturn(jwt);
        ResponseEntity<?> response = securityController.login(request);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        JwtResponse body = (JwtResponse) response.getBody();

        assertNotNull(body);
        assertEquals(jwt, body);
    }

    @Test
    void testRegistration_shouldReturnPlayerDTO() {
        final String login = "test";
        final String password = "test";
        final SecurityRequest request = new SecurityRequest(login, password);
        Player player = new Player(login, password, BigDecimal.ZERO);
        PlayerDTO playerDTO = new PlayerDTO(login, BigDecimal.ZERO);
        when(securityService.register(login, password)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(playerDTO);

        ResponseEntity<?> response = securityController.registration(request);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        PlayerDTO body = (PlayerDTO) response.getBody();

        assertNotNull(body);
        assertEquals(playerDTO.getLogin(), body.getLogin());
        assertEquals(playerDTO.getBalance().doubleValue(), body.getBalance().doubleValue());

    }
}