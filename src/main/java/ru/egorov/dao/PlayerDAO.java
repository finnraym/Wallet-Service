package ru.egorov.dao;

import ru.egorov.model.Player;

import java.math.BigDecimal;
import java.util.Optional;

public interface PlayerDAO extends GeneralDAO<Long, Player> {
    Optional<Player> findByLogin(String login);
    boolean updatePlayerBalance(Long id, BigDecimal balance);

}
