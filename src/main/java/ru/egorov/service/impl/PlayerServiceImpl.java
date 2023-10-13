package ru.egorov.service.impl;

import ru.egorov.dao.PlayerDAO;
import ru.egorov.exception.PlayerNotFoundException;
import ru.egorov.model.Player;
import ru.egorov.service.PlayerService;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * The type Player service.
 */
public class PlayerServiceImpl implements PlayerService {

    private final PlayerDAO playerDAO;

    /**
     * Instantiates a new Player service.
     *
     * @param playerDAO the player dao
     */
    public PlayerServiceImpl(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    public BigDecimal getPlayerBalance(Long id) {
        Optional<Player> optionalPlayer = playerDAO.findById(id);
        Player playerInMemory = optionalPlayer.orElseThrow(() -> new PlayerNotFoundException("The player with id " + id + " not found."));
        return playerInMemory.getBalance();
    }

    @Override
    public boolean updateBalance(Long id, BigDecimal balance) {
        return playerDAO.updatePlayerBalance(id, balance);
    }
}
