package ru.egorov.service;

import ru.egorov.in.dto.JwtResponse;
import ru.egorov.model.Player;

/**
 * The interface Security service.
 */
public interface SecurityService {
    /**
     * Register player.
     *
     * @param login    the login
     * @param password the password
     * @return the player
     */
    Player register(String login, String password);

    /**
     * Authorization player.
     *
     * @param login    the login
     * @param password the password
     * @return the player
     */
    JwtResponse authorization(String login, String password);
}
