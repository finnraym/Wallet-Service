package ru.egorov.exception;

/**
 * The type Player not found exception.
 */
public class PlayerNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Player not found exception.
     *
     * @param message the message
     */
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
