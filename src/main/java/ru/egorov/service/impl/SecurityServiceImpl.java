package ru.egorov.service.impl;

import ru.egorov.aop.annotations.Audit;
import ru.egorov.aop.annotations.Loggable;
import ru.egorov.dao.PlayerDAO;
import ru.egorov.exception.AuthorizeException;
import ru.egorov.exception.RegisterException;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.in.security.JwtTokenProvider;
import ru.egorov.model.Player;
import ru.egorov.service.SecurityService;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

/**
 * The type Security service.
 */
public class SecurityServiceImpl implements SecurityService {

    private final PlayerDAO playerDAO;

    private final JwtTokenProvider tokenProvider;

    /**
     * Instantiates a new Security service.
     *
     * @param playerDAO the player dao
     */
    public SecurityServiceImpl(PlayerDAO playerDAO, JwtTokenProvider tokenProvider) {
        this.playerDAO = playerDAO;
        this.tokenProvider = tokenProvider;
    }

    @Audit
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

    @Audit
    @Override
    public JwtResponse authorization(String login, String password) {
        Optional<Player> optionalPlayer = playerDAO.findByLogin(login);
        if (optionalPlayer.isEmpty()) {
            throw new AuthorizeException("There is no player with this login in the database.");
        }

        Player player = optionalPlayer.get();
        if (!player.getPassword().equals(password)) {
            throw new AuthorizeException("Incorrect password.");
        }

        String accessToken = tokenProvider.createAccessToken(login);
        String refreshToken = tokenProvider.createRefreshToken(login);
        try {
            tokenProvider.authentication(accessToken);
        } catch (AccessDeniedException e) {
            throw new AuthorizeException("Access denied!.");
        }
        return new JwtResponse(login, accessToken, refreshToken);
    }
}
