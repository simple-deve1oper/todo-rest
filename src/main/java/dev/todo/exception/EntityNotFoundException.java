package dev.todo.exception;

/**
 * Класс исключение для описания ошибок не найденных сущностей
 * @version 1.0
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {}

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
