package ru.egorov.service;

import ru.egorov.aop.annotations.Loggable;
import ru.egorov.model.Player;

import java.math.BigDecimal;

/**
 * The interface Player service.
 */
public interface PlayerService {

    /**
     * Gets player balance.
     *
     * @param id the id
     * @return the player balance
     */
    BigDecimal getPlayerBalance(Long id);

    /**
     * Update balance boolean.
     *
     * @param id      the id
     * @param balance the balance
     * @return the boolean
     */
    boolean updateBalance(Long id, BigDecimal balance);

    Player getByLogin(String login);

}
