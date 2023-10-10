package ru.egorov.dao;

import ru.egorov.model.Player;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * The interface Player dao.
 */
public interface PlayerDAO extends GeneralDAO<Long, Player> {
    /**
     * Find by login optional.
     *
     * @param login the login
     * @return the optional
     */
    Optional<Player> findByLogin(String login);

    /**
     * Update player balance boolean.
     *
     * @param id      the id
     * @param balance the balance
     * @return the boolean
     */
    boolean updatePlayerBalance(Long id, BigDecimal balance);

}
