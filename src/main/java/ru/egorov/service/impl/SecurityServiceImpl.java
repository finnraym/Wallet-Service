package ru.egorov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.exception.AuthorizeException;
import ru.egorov.exception.RegisterException;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.in.security.JwtTokenProvider;
import ru.egorov.model.Player;
import ru.egorov.repository.PlayerRepository;
import ru.egorov.service.SecurityService;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * The security service implementation
 */
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final PlayerRepository playerDAO;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Player register(String login, String password) {
        Optional<Player> player = playerDAO.findByLogin(login);
        if (player.isPresent()) {
            throw new RegisterException("The player with this login already exists.");
        }

        Player newPlayer = new Player();
        newPlayer.setBalance(new BigDecimal(0));
        newPlayer.setLogin(login);
        newPlayer.setPassword(passwordEncoder.encode(password));

        return playerDAO.save(newPlayer);
    }

    @Transactional
    @Override
    public JwtResponse authorization(String login, String password) {
        Optional<Player> optionalPlayer = playerDAO.findByLogin(login);
        if (optionalPlayer.isEmpty()) {
            throw new AuthorizeException("There is no player with this login in the database.");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

        String accessToken = tokenProvider.createAccessToken(login);
        String refreshToken = tokenProvider.createRefreshToken(login);
        return new JwtResponse(login, accessToken, refreshToken);
    }
}
