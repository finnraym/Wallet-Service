package ru.egorov.in.dto;

/**
 * Response for jwt
*
* @param login the player login
* @param accessToken the jwt access token
* @param refreshToken the refresh token
*
* */
public record JwtResponse(String login, String accessToken, String refreshToken) {
}
