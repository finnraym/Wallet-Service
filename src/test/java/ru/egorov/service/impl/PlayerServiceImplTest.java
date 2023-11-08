package ru.egorov.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egorov.exception.PlayerNotFoundException;
import ru.egorov.model.Player;
import ru.egorov.repository.PlayerRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The type Player service impl test.
 */
@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @InjectMocks
    private PlayerServiceImpl playerService;
    @Mock
    private PlayerRepository playerDAO;

    /**
     * Test get player balance success.
     */
    @Test
    void testGetPlayerBalance_Success() {
        Optional<Player> playerOptional = Optional.of(new Player("login", "password", BigDecimal.valueOf(1000)));
        Mockito.when(playerDAO.findById(1L)).thenReturn(playerOptional);

        BigDecimal playerBalance = playerService.getPlayerBalance(1L);
        assertEquals(playerOptional.get().getBalance(), playerBalance);
    }

    /**
     * Test get player balance throw exception.
     */
    @Test
    void testGetPlayerBalance_ThrowException() {
        Mockito.when(playerDAO.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> playerService.getPlayerBalance(1L));
    }
}