package ru.egorov.dao.impl;

import ru.egorov.dao.PlayerDAO;
import ru.egorov.model.Player;

import java.math.BigDecimal;
import java.util.*;

public class InMemoryPlayerDAO implements PlayerDAO {
    private final Map<Long, Player> players = new HashMap<>();
    private Long id = 1L;
    @Override
    public Optional<Player> findById(Long id) {
        Player player = players.get(id);
        return player == null ? Optional.empty() : Optional.of(player);
    }

    @Override
    public Optional<Player> findByLogin(String login) {
        Player player = null;
        List<Player> list = (List<Player>) players.values();

        for (Player pl : list) {
            if (pl.getLogin().equals(login)) {
                player = pl;
                break;
            }
        }

        return player == null ? Optional.empty() : Optional.of(player);
    }

    @Override
    public List<Player> findAll() {
        return (List<Player>) players.values();
    }

    @Override
    public Player save(Player player) {
        player.setId(getLastId());
        incrementId();
        players.put(player.getId(), player);
        return players.get(player.getId());
    }

    @Override
    public boolean updatePlayerBalance(Long id, BigDecimal balance) {
        Player playerInMemory = players.get(id);
        if (playerInMemory == null) return false;
        playerInMemory.setBalance(balance);

        return true;
    }

    private Long getLastId() {
        return id;
    }
    private void incrementId() {
        id++;
    }

}
