package ru.egorov.in.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ru.egorov.in.dto.JwtResponse;
import ru.egorov.model.Player;
import ru.egorov.service.PlayerService;

import java.nio.file.AccessDeniedException;
import java.security.Key;
import java.util.Date;

public class JwtTokenProvider {
    private final Long access;
    private final Long refresh;
    private final Key key;
    private final PlayerService playerService;

    public JwtTokenProvider(String secret, Long access, Long refresh, PlayerService playerService) {
        this.access = access;
        this.refresh = refresh;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.playerService = playerService;
    }

    public String createAccessToken(String login) {
        Claims claims = Jwts.claims().setSubject(login);
        Date now = new Date();
        Date validity = new Date(now.getTime() + access);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String login) {
        Claims claims = Jwts.claims().setSubject(login);
        Date now = new Date();
        Date validity = new Date(now.getTime() + refresh);
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

    public Authentication authentication(String token) throws AccessDeniedException {
        if (!validateToken(token)) {
            throw new AccessDeniedException("Access denied!");
        }
        String login = getLoginFromToken(token);
        playerService.getByLogin(login);
        return new Authentication(login, true, null);
    }

    private String getLoginFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateToken(String token) throws RuntimeException {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return !claims.getBody().getExpiration().before(new Date());
    }
}
