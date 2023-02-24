package dev.todo.util;

import dev.todo.dto.NoteDTO;
import dev.todo.dto.PersonDTO;
import dev.todo.dto.TaskDTO;
import dev.todo.entity.Note;
import dev.todo.entity.Person;
import dev.todo.entity.Task;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для перевода данных из сущности в DTO и наоборот
 * @version 1.0
 */
public class DataTransformation {
    /**
     * Перевод данных объекта типа Note из сущности в DTO
     * @param note - объект типа Note
     * @return объект DTO типа Note
     */
    public static NoteDTO convertingNoteDataFromEntityToDTO(Note note) {
        Long id = note.getId();
        String title = note.getTitle();
        LocalDateTime dateTime = note.getDateTime();
        List<TaskDTO> tasksDTO = null;
        if (note.getTasks() == null) {
            tasksDTO = Collections.emptyList();
        } else {
            tasksDTO = convertingListTasksFromEntityToDTO(note.getTasks());
        }

        NoteDTO noteDTO = new NoteDTO(id, title, dateTime, tasksDTO);

        return noteDTO;
    }

    /**
     * Перевод данных списка объектов типа Note из сущности в DTO
     * @param notes - список объектов типа Note
     * @return список объектов DTO типа Note
     */
    public static List<NoteDTO> convertingListNotesFromEntityToDTO(List<Note> notes) {
        return notes.stream()
                .map(note -> convertingNoteDataFromEntityToDTO(note))
                .collect(Collectors.toList());
    }

    /**
     * Перевод данных объекта типа Task из сущности в DTO
     * @param task - объект типа Task
     * @return объект DTO типа Task
     */
    public static TaskDTO convertingTaskDataFromEntityToDTO(Task task) {
        Long id = task.getId();
        String name = task.getName();
        Boolean done = task.getDone();
        Long noteId = task.getNote().getId();

        TaskDTO taskDTO = new TaskDTO(id, name, done, noteId);

        return taskDTO;
    }

    /**
     * Перевод данных списка объектов типа Task из сущности в DTO
     * @param tasks - список объектов типа Task
     * @return список объектов DTO типа Task
     */
    public static List<TaskDTO> convertingListTasksFromEntityToDTO(List<Task> tasks) {
        return tasks.stream()
                .map(task -> convertingTaskDataFromEntityToDTO(task))
                .collect(Collectors.toList());
    }

    /**
     * Перевод данных объекта типа Task из DTO в сущность
     * @param personDTO - объект DTO типа Person
     * @return объект типа Person
     */
    public static Person convertingPersonDataFromDtoToEntity(PersonDTO personDTO) {
        Long id = personDTO.getId();
        String username = personDTO.getUsername();
        String password = personDTO.getPassword();

        Person person = new Person(id, username, password);

        return person;
    }
}
