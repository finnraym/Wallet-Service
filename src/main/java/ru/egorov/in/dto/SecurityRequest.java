package ru.egorov.in.dto;

import jakarta.validation.constraints.NotNull;
public record SecurityRequest(@NotNull(message = "Login must be not null.") String login,
                              @NotNull(message = "Password must be not null.") String password) {
}
