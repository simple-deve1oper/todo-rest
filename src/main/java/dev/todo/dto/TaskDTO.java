package dev.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Класс для описания DTO типа Task
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class TaskDTO {
    private Long id;    // идентификатор
    @NotBlank(message = "Наименование задачи должно быть заполнено")
    private String name;    // наименование
    private Boolean done;   // статус выполнения задачи
    @NotNull(message = "Номер записи для задачи должен быть заполнен")
    @Min(value = 1, message = "Минимальное значение идентификатора записи 1")
    private Long noteId;    // идентификатор записи

    public TaskDTO(String name, Long noteId) {
        this.name = name;
        this.noteId = noteId;
        this.done = false;
    }
}
