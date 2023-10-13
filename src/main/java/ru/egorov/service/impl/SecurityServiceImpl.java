package ru.egorov.service.impl;

import ru.egorov.dao.PlayerDAO;
import ru.egorov.exception.AuthorizeException;
import ru.egorov.exception.RegisterException;
import ru.egorov.model.Player;
import ru.egorov.service.SecurityService;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * The type Security service.
 */
public class SecurityServiceImpl implements SecurityService {

    private final PlayerDAO playerDAO;

    /**
     * Instantiates a new Security service.
     *
     * @param playerDAO the player dao
     */
    public SecurityServiceImpl(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    public Player register(String login, String password) {
        Optional<Player> player = playerDAO.findByLogin(login);
        if (player.isPresent()) {
            throw new RegisterException("The player with this login already exists.");
        }

        Player newPlayer = new Player();
        newPlayer.setBalance(new BigDecimal(0));
        newPlayer.setLogin(login);
        newPlayer.setPassword(password);

        return playerDAO.save(newPlayer);
    }

    @Override
    public Player authorization(String login, String password) {
        Optional<Player> optionalPlayer = playerDAO.findByLogin(login);
        if (optionalPlayer.isEmpty()) {
            throw new AuthorizeException("There is no player with this login in the database.");
        }

        Player player = optionalPlayer.get();
        if (!player.getPassword().equals(password)) {
            throw new AuthorizeException("Incorrect password.");
        }

        return player;
    }
}
