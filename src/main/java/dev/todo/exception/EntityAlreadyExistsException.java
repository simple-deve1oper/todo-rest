package dev.todo.exception;

/**
 * Класс исключение для описания ошибок с существующими сущностями
 * @version 1.0
 */
public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException() {}

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
