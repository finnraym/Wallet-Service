package ru.egorov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.auditspringbootstarter.Auditable;
import ru.egorov.exception.PlayerNotFoundException;
import ru.egorov.loggingspringbootstarter.Loggable;
import ru.egorov.model.Player;
import ru.egorov.repository.PlayerRepository;
import ru.egorov.service.PlayerService;

import java.math.BigDecimal;


/**
 * The player service implementation
 */
@Service
@RequiredArgsConstructor
@Loggable
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Auditable
    @Transactional(readOnly = true)
    @Override
    public BigDecimal getPlayerBalance(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("The player with id " + id + " not found."))
                .getBalance();
    }

    @Transactional
    @Override
    public void updateBalance(Long id, BigDecimal balance) {
        Player byId = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("The player with id " + id + " not found."));
        byId.setBalance(balance);
        playerRepository.save(byId);
    }

    @Transactional(readOnly = true)
    @Override
    public Player getByLogin(String login) {
        return playerRepository.findByLogin(login)
                .orElseThrow(() -> new PlayerNotFoundException("Player with login " + login + " not found!"));
    }
}
