package ru.egorov.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egorov.exception.AuthorizeException;
import ru.egorov.exception.RegisterException;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.model.Player;
import ru.egorov.repository.PlayerRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * The type Security service impl test.
 */
@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @InjectMocks
    private SecurityServiceImpl securityService;
    @Mock
    private PlayerRepository playerDAO;

    /**
     * Test register success.
     */
    @Test
    void testRegister_Success() {
        String login = "login";
        String password = "password";
        Player player = new Player(login, password, BigDecimal.ZERO);
        Mockito.when(playerDAO.findByLogin(login)).thenReturn(Optional.empty());
        Mockito.when(playerDAO.save(any(Player.class))).thenReturn(player);

        Player registerPlayer = securityService.register(login, password);
        assertEquals(login, registerPlayer.getLogin());
        assertEquals(password, registerPlayer.getPassword());
    }

    /**
     * Test register throw exception.
     */
    @Test
    void testRegister_ThrowException() {
        String login = "login";
        String password = "password";
        Player player = new Player(login, password, BigDecimal.ZERO);
        Mockito.when(playerDAO.findByLogin(login)).thenReturn(Optional.of(player));

        assertThrows(RegisterException.class, () -> securityService.register(login, password));
    }

    /**
     * Test authorization success.
     */
    @Test
    void testAuthorization_Success() {
        String login = "login";
        String password = "password";
        Player player = new Player(login, password, BigDecimal.ZERO);
        Mockito.when(playerDAO.findByLogin(login)).thenReturn(Optional.of(player));

        JwtResponse authorization = securityService.authorization(login, password);
    }

    /**
     * Test authorization throw exception.
     */
    @Test
    void testAuthorization_ThrowException() {
        String login = "login";
        String password = "password";
        Mockito.when(playerDAO.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(AuthorizeException.class, () -> securityService.authorization(login, password));
    }
}