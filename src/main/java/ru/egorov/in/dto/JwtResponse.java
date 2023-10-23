package ru.egorov.in.dto;

public record JwtResponse(String login, String accessToken, String refreshToken) {
}
