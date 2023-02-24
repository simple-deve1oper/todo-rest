package dev.todo.util;

import dev.todo.dto.NoteDTO;
import dev.todo.dto.PersonDTO;
import dev.todo.dto.TaskDTO;
import dev.todo.entity.Note;
import dev.todo.entity.Person;
import dev.todo.entity.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class DataTransformationTest {
    @Test
    void convertingNoteDataFromEntityToDTO() {
        Person person = new Person(1L, "test", "test123");
        LocalDateTime dateTime = LocalDateTime.now();
        Note note = new Note(1L, "Список задач", dateTime, person, Collections.emptyList());
        NoteDTO noteDTO = DataTransformation.convertingNoteDataFromEntityToDTO(note);

        Assertions.assertTrue(noteDTO instanceof NoteDTO);
        Assertions.assertEquals(1, noteDTO.getId());
        Assertions.assertEquals(dateTime.toString(), noteDTO.getDateTime().toString());
        Assertions.assertEquals(Collections.emptyList(), noteDTO.getTasks());

        LocalDateTime dateTime2 = LocalDateTime.now();
        Note note2 = new Note(2L, "Тест", dateTime2, person, Collections.emptyList());
        List<Task> tasks = Arrays.asList(new Task(1L, "Сыр", true, note2), new Task(2L, "Молоко", false, note2));
        note2.setTasks(tasks);
        NoteDTO note2DTO = DataTransformation.convertingNoteDataFromEntityToDTO(note2);

        Assertions.assertTrue(note2DTO instanceof NoteDTO);
        Assertions.assertEquals(2, note2DTO.getId());
        Assertions.assertEquals(dateTime2.toString(), note2DTO.getDateTime().toString());
        Assertions.assertEquals(2, note2DTO.getTasks().size());
    }

    @Test
    void convertingListNotesFromEntityToDTO() {
        Person person = new Person(1L, "test", "test123");
        Note note = new Note(1L, "Список задач", LocalDateTime.now(), person, Collections.emptyList());
        Note note2 = new Note(2L, "Тест", LocalDateTime.now(), person, Collections.emptyList());
        List<Task> tasks = Arrays.asList(new Task(1L, "Сыр", true, note2), new Task(2L, "Молоко", false, note2));
        note2.setTasks(tasks);
        List<NoteDTO> notesDTO = DataTransformation.convertingListNotesFromEntityToDTO(Arrays.asList(note, note2));

        notesDTO.forEach(noteDTO -> Assertions.assertTrue(noteDTO instanceof NoteDTO));
        Assertions.assertEquals(2, notesDTO.size());
    }

    @Test
    void convertingTaskDataFromEntityToDTO() {
        Person person = new Person(1L, "test", "test123");
        Note note = new Note(1L, "Список задач", LocalDateTime.now(), person, Collections.emptyList());
        Task task = new Task(1L, "Сыр", true, note);
        note.setTasks(Arrays.asList(task));

        TaskDTO taskDTO = DataTransformation.convertingTaskDataFromEntityToDTO(task);
        Assertions.assertTrue(taskDTO instanceof TaskDTO);
        Assertions.assertEquals(1L, taskDTO.getId());
        Assertions.assertEquals("Сыр", taskDTO.getName());
        Assertions.assertTrue(taskDTO.getDone());
        Assertions.assertNotNull(note);
    }

    @Test
    void convertingListTasksFromEntityToDTO() {
        Person person = new Person(1L, "test", "test123");
        Note note = new Note(1L, "Список задач", LocalDateTime.now(), person, Collections.emptyList());
        Task task = new Task(1L, "Сыр", true, note);
        Task task2 = new Task(2L, "Молоко", false, note);
        List<Task> tasks = Arrays.asList(task, task2);
        List<TaskDTO> tasksDTO = DataTransformation.convertingListTasksFromEntityToDTO(tasks);

        tasksDTO.forEach(taskDTO -> Assertions.assertTrue(taskDTO instanceof TaskDTO));
        Assertions.assertEquals(2, tasksDTO.size());
    }

    @Test
    void convertingPersonDataFromDtoToEntity() {
        PersonDTO personDTO = new PersonDTO(1L, "test", "test123");
        Person person = DataTransformation.convertingPersonDataFromDtoToEntity(personDTO);

        Assertions.assertEquals(1, person.getId());
        Assertions.assertEquals("test", person.getUsername());
        Assertions.assertEquals("test123", person.getPassword());
    }
}