package ru.egorov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.exception.AuthorizeException;
import ru.egorov.model.Player;
import ru.egorov.repository.PlayerRepository;

import java.util.Collections;
import java.util.Optional;


/**
 * The user details service implementation
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PlayerRepository playerDAO;


    /**
     * @param username the username identifying the user whose data is required.
     * @return user details
     * @throws UsernameNotFoundException when user with username not found
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Player> optionalPlayer = playerDAO.findByLogin(username);
        if (optionalPlayer.isEmpty()) {
            throw new AuthorizeException("There is no player with this login in the database.");
        }
        Player player = optionalPlayer.get();
        return new User(
                player.getLogin(),
                player.getPassword(),
                Collections.emptyList()
        );
    }
}
