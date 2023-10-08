package ru.egorov.service.impl;

import ru.egorov.dao.PlayerDAO;
import ru.egorov.exception.PlayerAlreadyExistsException;
import ru.egorov.model.Player;
import ru.egorov.service.SecurityService;

import java.math.BigDecimal;
import java.util.Optional;

public class SecurityServiceImpl implements SecurityService {

    private final PlayerDAO playerDAO;

    public SecurityServiceImpl(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    public Player register(String login, String password) {
        Optional<Player> player = playerDAO.findByLogin(login);
        if (player.isPresent()) {
            throw new PlayerAlreadyExistsException("Игрок с таким логином уже существует.");
        }

        Player newPlayer = new Player();
        newPlayer.setBalance(new BigDecimal(0));
        newPlayer.setLogin(login);
        newPlayer.setPassword(password);

        return playerDAO.save(newPlayer);
    }

    @Override
    public Player authorization(String login, String password) {
        return null;
    }
}
