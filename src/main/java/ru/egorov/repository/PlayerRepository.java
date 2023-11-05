package ru.egorov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.egorov.model.Player;

import java.util.Optional;

/**
 * The player repository
 */
public interface PlayerRepository extends JpaRepository<Player, Long> {
    /**
     * Find player in database by login
     *
     * @param login the login
     * @return optional of player. If not fount optional is empty
     */
    Optional<Player> findByLogin(String login);
}
