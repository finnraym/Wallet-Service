package ru.egorov.in.dto;

import javax.validation.constraints.NotNull;

/**
 * Request data for security
 *
 * @param login the player login
 * @param password the player password
 */
public record SecurityRequest(@NotNull(message = "Login must be not null.") String login,
                              @NotNull(message = "Password must be not null.") String password) {
}
