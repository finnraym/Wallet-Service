package ru.egorov.in.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.service.PlayerService;

import java.nio.file.AccessDeniedException;
import java.security.Key;
import java.util.Date;

/**
 * The service for working with jwt
 */
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties properties;
    private final UserDetailsService userDetailsService;
    private final PlayerService playerService;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes());
    }

    /**
     * Create access token for jwt
     *
     * @param login the player login
     * @return access token string
     */
    public String createAccessToken(String login) {
        Claims claims = Jwts.claims().setSubject(login);
        Date now = new Date();
        Date validity = new Date(now.getTime() + properties.getAccess());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    /**
     * Create refresh token for jwt
     *
     * @param login the player login
     * @return refresh token string
     */
    public String createRefreshToken(String login) {
        Claims claims = Jwts.claims().setSubject(login);
        Date now = new Date();
        Date validity = new Date(now.getTime() + properties.getRefresh());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) throws AccessDeniedException {
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException("Access denied!");
        }

        String login = getLoginFromToken(refreshToken);
        playerService.getByLogin(login);

        return new JwtResponse(login, createAccessToken(login), createRefreshToken(login));
    }

    /**
     * Authentication player by jwt
     *
     * @param token the jwt token
     * @return authentication
     * @throws AccessDeniedException
     */
    public Authentication authentication(String token) throws AccessDeniedException {
        String username = getLoginFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getLoginFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    /**
     * Validate jwt token
     *
     * @param token the jwt token
     * @return true if token is valid and false else
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (RuntimeException e) {
            return false;
        }
    }
}
