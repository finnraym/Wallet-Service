package ru.egorov.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.egorov.config.DBConnectionProvider;
import ru.egorov.config.DBMigrationService;
import ru.egorov.model.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcPlayerDAOTest extends JdbcAbstractDAOTest {

    private JdbcPlayerDAO playerDAO;
    @BeforeEach
    void setUp() {
        DBConnectionProvider connectionProvider = new DBConnectionProvider(
                container.getJdbcUrl(), container.getUsername(), container.getPassword(),
                "org.postgresql.Driver");

        DBMigrationService migrationService = new DBMigrationService(connectionProvider, "migration", "db.changelog/changelog.xml");
        migrationService.migration();

        playerDAO = new JdbcPlayerDAO(connectionProvider);
    }
    @Test
    void testFindById_success() {
        Optional<Player> optionalPlayer = playerDAO.findById(1L);
        assertTrue(optionalPlayer.isPresent());
        Player result = optionalPlayer.get();
        Player expected = new Player("testplayer1", "testplayer1", BigDecimal.valueOf(125.0));

        assertEquals(expected.getLogin(), result.getLogin());
        assertEquals(expected.getPassword(), result.getPassword());
        assertEquals(expected.getBalance().doubleValue(), result.getBalance().doubleValue());
    }

    @Test
    void testFindById_notFound() {
        Optional<Player> optionalPlayer = playerDAO.findById(100L);
        assertTrue(optionalPlayer.isEmpty());
    }

    @Test
    void testFindAll() {
        List<Player> all = playerDAO.findAll();
        assertEquals(3, all.size());
    }

    @Test
    void testSave_success() {
        Player saved = new Player("testplayer2", "testplayer2", BigDecimal.ZERO);
        Player result = playerDAO.save(saved);

        assertEquals(saved.getLogin(), result.getLogin());
        assertEquals(saved.getPassword(), result.getPassword());
        assertEquals(saved.getBalance().doubleValue(), result.getBalance().doubleValue());
    }

    @Test
    void testSave_failed() {
        Player saved = new Player("testplayer2", "testplayer2", BigDecimal.ZERO);
        saved.setLogin(null);
        assertThrows(RuntimeException.class, () -> playerDAO.save(saved));
    }

    @Test
    void testFindByLogin_success() {
        Optional<Player> optionalPlayer = playerDAO.findByLogin("testplayer1");
        assertTrue(optionalPlayer.isPresent());
        Player result = optionalPlayer.get();
        Player expected = new Player("testplayer1", "testplayer1", BigDecimal.valueOf(125.0));

        assertEquals(expected.getLogin(), result.getLogin());
        assertEquals(expected.getPassword(), result.getPassword());
        assertEquals(expected.getBalance().doubleValue(), result.getBalance().doubleValue());
    }

    @Test
    void testFindByLogin_notFound() {
        Optional<Player> optionalPlayer = playerDAO.findByLogin("testplayer10");
        assertTrue(optionalPlayer.isEmpty());
    }

    @Test
    void testUpdatePlayerBalance_success() {
        boolean returned = playerDAO.updatePlayerBalance(1L, BigDecimal.valueOf(1000));
        Optional<Player> playerOptional = playerDAO.findById(1L);
        assertTrue(playerOptional.isPresent());
        Player player = playerOptional.get();

        assertEquals(BigDecimal.valueOf(1000).doubleValue(), player.getBalance().doubleValue());
    }
}