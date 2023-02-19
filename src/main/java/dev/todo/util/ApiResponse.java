package dev.todo.util;

import java.time.LocalDateTime;

/**
 * Класс для представления ответа
 * @param code - код
 * @param message - сообщение
 * @param dateTime - дата и время
 */
public record ApiResponse(int code, String message, LocalDateTime dateTime) {
    public ApiResponse(int code, String message) {
        this(code, message, LocalDateTime.now());
    }
}
