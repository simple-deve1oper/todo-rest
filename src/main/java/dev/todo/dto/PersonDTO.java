package dev.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Класс для описания DTO типа Person
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class PersonDTO {
    private Long id;    // идентификатор
    @NotBlank(message = "Имя пользователя должно быть заполнено")
    @Pattern(regexp = "[a-z0-9]+", message = "Имя пользователя может содержать только латинские строчные символы и цифры")
    private String username;    // имя пользователя
    @NotBlank(message = "Пароль должен быть заполнен")
    private String password;    // пароль

    public PersonDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
