package ru.egorov.service;

import ru.egorov.in.dto.JwtResponse;
import ru.egorov.model.Player;

/**
 * The interface security service.
 */
public interface SecurityService {

    /**
     * Register the player in application
     *
     * @param login the login
     * @param password the password
     * @return the registered player
     */
    Player register(String login, String password);

    /**
     * Authorization player in application
     *
     * @param login the login
     * @param password the password
     * @return jwt response
     */
    JwtResponse authorization(String login, String password);
}
