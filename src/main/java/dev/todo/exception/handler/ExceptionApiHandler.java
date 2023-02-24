package dev.todo.exception.handler;

import dev.todo.exception.EntityAlreadyExistsException;
import dev.todo.exception.EntityNotFoundException;
import dev.todo.exception.EntityValidationException;
import dev.todo.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Класс для описания обработчика исключений
 * @version 1.0
 */
@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler
    public ResponseEntity<ApiResponse> entityNotFound(EntityNotFoundException exception) {
        var code = HttpStatus.NOT_FOUND.value();
        var message = exception.getMessage();
        ApiResponse response = createResponse(code, message);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse> entityAlreadyExists(EntityAlreadyExistsException exception) {
        var code = HttpStatus.CONFLICT.value();
        var message = exception.getMessage();
        ApiResponse response = createResponse(code, message);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse> validation(EntityValidationException exception) {
        var code = HttpStatus.BAD_REQUEST.value();
        var message = exception.getMessage();
        ApiResponse responseError = createResponse(code, message);

        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse> userNotFound(UsernameNotFoundException exception) {
        var code = HttpStatus.NOT_FOUND.value();
        var message = exception.getMessage();
        ApiResponse response = createResponse(code, message);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private ApiResponse createResponse(int code, String message) {
        return new ApiResponse(code, message);
    }
}
