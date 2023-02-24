package dev.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.todo.dto.NoteDTO;
import dev.todo.entity.Note;
import dev.todo.entity.Person;
import dev.todo.exception.EntityAlreadyExistsException;
import dev.todo.exception.EntityNotFoundException;
import dev.todo.security.PersonDetails;
import dev.todo.security.PersonDetailsService;
import dev.todo.service.NoteService;
import dev.todo.service.PersonService;
import dev.todo.service.TaskService;
import dev.todo.util.DataTransformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NoteController.class)
public class NoteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NoteService noteService;
    @MockBean
    private TaskService taskService;
    @MockBean
    private PersonService personService;
    @MockBean
    private PersonDetailsService personDetailsService;
    @MockBean
    private PersonDetails personDetails;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "test", password = "test123")
    public void noteController_getAllNotesByPersonId() throws Exception {
        Person person = new Person(1L, "test", "test123");

        Mockito.when(personService.getPersonByUsername(Mockito.any(String.class))).thenReturn(person);

        List<Note> notes = Arrays.asList(
                new Note(1L, "Список задач 1", LocalDateTime.now(), person, Collections.emptyList()),
                new Note(2L, "Список задач 2", LocalDateTime.now(), person, Collections.emptyList())
        );
        Mockito.when(noteService.getAllNotesByPersonId(Mockito.any(Long.class)))
                .thenReturn(notes);

        List<NoteDTO> notesDTO = DataTransformation.convertingListNotesFromEntityToDTO(notes);
        String responseJson = objectMapper.writeValueAsString(notesDTO);

        mockMvc.perform(get("/api/v1/notes/all")
                        .contentType(MediaType.APPLICATION_JSON).with(user("test")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Список задач 1", "Список задач 2")));
    }

    @Test
    @WithMockUser(username = "test", password = "test123")
    public void noteController_getNoteById() throws Exception {
        Person person = new Person("test", "test123");

        Note note = new Note(1L, "Список задач 1", LocalDateTime.now(), person, Collections.emptyList());
        Mockito.when(noteService.getNoteById(1L))
                .thenReturn(note);

        NoteDTO noteDTO = DataTransformation.convertingNoteDataFromEntityToDTO(note);
        String responseJson = objectMapper.writeValueAsString(noteDTO);

        mockMvc.perform(get("/api/v1/notes/1")
                        .contentType(MediaType.APPLICATION_JSON).with(user("test")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.title", equalTo("Список задач 1")));

        Mockito.when(noteService.getNoteById(2L))
                .thenThrow(new EntityNotFoundException("Запись с идентификатором 2 не найдена"));

        mockMvc.perform(get("/api/v1/notes/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Запись с идентификатором 2 не найдена")));

    }

    @Test
    @WithMockUser(username = "test", password = "test123")
    public void noteController_addNote() throws Exception {
        Person person = new Person(1L, "test", "test123");
        Note temp = new Note("Список задач 1", person);
        Note note = new Note(1L, "Список задач 1", LocalDateTime.now(), person, Collections.emptyList());
        Mockito.when(noteService.addNote(Mockito.any(Note.class)))
                .thenReturn(note);

        NoteDTO noteDTO = DataTransformation.convertingNoteDataFromEntityToDTO(note);
        String requestJSON = objectMapper.writeValueAsString(DataTransformation.convertingNoteDataFromEntityToDTO(temp));
        String responseJson = objectMapper.writeValueAsString(noteDTO);

        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.title", equalTo("Список задач 1")));

        Mockito.when(noteService.addNote(Mockito.any(Note.class)))
                .thenThrow(new EntityAlreadyExistsException("Запись с наименованием 'Список задач 1' уже существует"));

        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(409)))
                .andExpect(jsonPath("$.message", equalTo("Запись с наименованием 'Список задач 1' уже существует")));
    }

    @Test
    @WithMockUser(username = "test", password = "test123")
    public void noteController_updateNoteTitle() throws Exception {
        Person person = new Person(1L, "test", "test123");
        Note note = new Note(1L, "Тест", LocalDateTime.now(), person, Collections.emptyList());
        Mockito.when(noteService.updateNoteTitle(1L, "Тест"))
                .thenReturn(note);

        NoteDTO noteDTO = DataTransformation.convertingNoteDataFromEntityToDTO(note);
        String responseJson = objectMapper.writeValueAsString(noteDTO);

        mockMvc.perform(patch("/api/v1/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "Тест"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.title", equalTo("Тест")));

        Mockito.when(noteService.updateNoteTitle(2L, "Тест"))
                .thenThrow(new EntityNotFoundException("Запись с идентификатором 2 не найдена"));

        mockMvc.perform(patch("/api/v1/notes/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "Тест"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Запись с идентификатором 2 не найдена")));
    }

    @Test
    @WithMockUser(username = "test", password = "test123")
    public void noteController_deleteNoteById() throws Exception {
        Mockito.doNothing().when(noteService).deleteNoteById(1L);

        mockMvc.perform(delete("/api/v1/notes/1")).andExpect(status().isOk());

        Mockito.doThrow(EntityNotFoundException.class).when(noteService).deleteNoteById(2L);

        mockMvc.perform(delete("/api/v1/notes/2")).andExpect(status().isNotFound());
    }
}
