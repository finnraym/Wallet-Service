package ru.egorov.dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.egorov.model.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryPlayerDAOTest {

    private InMemoryPlayerDAO playerDAO;
    static private Player player1;
    static private Player player2;
    static private Player player3;

    @BeforeAll
    static void init() {
        player1 = new Player("GOSHAN", "goshka111", BigDecimal.valueOf(2000));
        player2 = new Player("KIRPICH", "red3", BigDecimal.valueOf(120));
        player3 = new Player("LEILA", "12345", BigDecimal.valueOf(999));
    }
    @BeforeEach
    void refresh() {
        playerDAO = new InMemoryPlayerDAO();
        playerDAO.save(player1);
        playerDAO.save(player2);
        playerDAO.save(player3);
    }

    @AfterEach
    void tearDown() {
        playerDAO = null;
    }
    @Test
    void testFindById_Success() {
        Optional<Player> byId = playerDAO.findById(1L);
        Player result = byId.get();
        Player expected = player1;
        assertNotNull(result);
        assertEquals(result.getLogin(), expected.getLogin());
        assertEquals(result.getPassword(), expected.getPassword());
        assertEquals(result.getBalance(), expected.getBalance());
    }
    @Test
    void testFindById_Empty() {
        Optional<Player> byId = playerDAO.findById(10L);
        Player result = byId.orElse(null);

        assertNull(result);
    }

    @Test
    void testFindByLogin_Success() {
        Optional<Player> byId = playerDAO.findByLogin("GOSHAN");
        Player result = byId.get();
        Player expected = player1;
        assertNotNull(result);
        assertEquals(result.getLogin(), expected.getLogin());
        assertEquals(result.getPassword(), expected.getPassword());
        assertEquals(result.getBalance(), expected.getBalance());
    }

    @Test
    void testFindByLogin_Empty() {
        Optional<Player> byId = playerDAO.findByLogin("Buse4ka");
        Player result = byId.orElse(null);

        assertNull(result);
    }

    @Test
    void testFindAll() {
        List<Player> all = playerDAO.findAll();
        assertEquals(3, all.size());
    }

    @Test
    void testSave() {
        Player newPlayer = new Player("aboba", "7777", BigDecimal.valueOf(0));
        Player result = playerDAO.save(newPlayer);

        assertEquals(newPlayer.getLogin(), result.getLogin());
        assertEquals(newPlayer.getPassword(), result.getPassword());
        assertEquals(newPlayer.getBalance(), result.getBalance());
    }

    @Test
    void testUpdatePlayerBalance_Success() {
        BigDecimal newAmount = BigDecimal.valueOf(666);
        boolean result = playerDAO.updatePlayerBalance(1L, newAmount);
        assertTrue(result);
    }

    @Test
    void testUpdatePlayerBalance_Failed() {
        BigDecimal newAmount = BigDecimal.valueOf(666);
        boolean result = playerDAO.updatePlayerBalance(12L, newAmount);
        assertFalse(result);
    }
}