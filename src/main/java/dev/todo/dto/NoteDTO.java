package dev.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс для описания DTO типа Note
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class NoteDTO {
    private Long id;    // идентификатор
    @NotBlank(message = "Наименование записи должно быть заполнено")
    private String title;   // наименование
    private LocalDateTime dateTime; // дата и время
    private List<TaskDTO> tasks;    // список задач
}
