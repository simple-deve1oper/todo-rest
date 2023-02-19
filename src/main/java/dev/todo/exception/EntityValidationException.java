package dev.todo.exception;

/**
 * Класс исключение для описания ошибок сущностей, которые не прошли валидацию
 * @version 1.0
 */
public class EntityValidationException extends RuntimeException {
    public EntityValidationException() {}

    public EntityValidationException(String message) {
        super(message);
    }

    public EntityValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityValidationException(Throwable cause) {
        super(cause);
    }
}
