package ru.egorov.exception;

/**
 * The type Player already exists exception.
 */
public class PlayerAlreadyExistsException extends RuntimeException {
    /**
     * Instantiates a new Player already exists exception.
     *
     * @param message the message
     */
    public PlayerAlreadyExistsException(String message) {
        super(message);
    }
}
