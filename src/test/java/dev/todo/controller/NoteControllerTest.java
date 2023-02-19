package dev.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.todo.dto.NoteDTO;
import dev.todo.entity.Note;
import dev.todo.exception.EntityAlreadyExistsException;
import dev.todo.exception.EntityNotFoundException;
import dev.todo.exception.EntityValidationException;
import dev.todo.service.NoteService;
import dev.todo.util.DataTransformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NoteController.class)
public class NoteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NoteService noteService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void noteController_getAllNotes() throws Exception {
        List<Note> notes = Arrays.asList(
                new Note(1L, "Список задач 1", LocalDateTime.now(), Collections.emptyList()),
                new Note(2L, "Список задач 2", LocalDateTime.now(), Collections.emptyList())
        );
        Mockito.when(noteService.getAllNotes())
                .thenReturn(notes);

        List<NoteDTO> notesDTO = DataTransformation.convertingListNotesFromEntityToDTO(notes);
        String responseJson = objectMapper.writeValueAsString(notesDTO);

        mockMvc.perform(get("/api/v1/notes/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Список задач 1", "Список задач 2")));
    }

    @Test
    public void noteController_getNoteById() throws Exception {
        Note note = new Note(1L, "Список задач 1", LocalDateTime.now(), Collections.emptyList());
        Mockito.when(noteService.getNoteById(1L))
                .thenReturn(note);

        NoteDTO noteDTO = DataTransformation.convertingNoteDataFromEntityToDTO(note);
        String responseJson = objectMapper.writeValueAsString(noteDTO);

        mockMvc.perform(get("/api/v1/notes/1")
                        .contentType(MediaType.APPLICATION_JSON))
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
    public void noteController_addNote() throws Exception {
        Note temp = new Note("Список задач 1");
        Note note = new Note(1L, "Список задач 1", LocalDateTime.now(), Collections.emptyList());
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
    public void noteController_updateNoteTitle() throws Exception {
        Note note = new Note(1L, "Тест", LocalDateTime.now(), Collections.emptyList());
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
    public void noteController_deleteNoteById() throws Exception {
        Mockito.doNothing().when(noteService).deleteNoteById(1L);

        mockMvc.perform(delete("/api/v1/notes/1")).andExpect(status().isOk());

        Mockito.doThrow(EntityNotFoundException.class).when(noteService).deleteNoteById(2L);

        mockMvc.perform(delete("/api/v1/notes/2")).andExpect(status().isNotFound());
    }
}
