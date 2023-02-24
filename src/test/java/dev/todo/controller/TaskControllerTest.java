package dev.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.todo.dto.TaskDTO;
import dev.todo.entity.Note;
import dev.todo.entity.Person;
import dev.todo.entity.Task;
import dev.todo.exception.EntityNotFoundException;
import dev.todo.security.PersonDetailsService;
import dev.todo.service.NoteService;
import dev.todo.service.PersonService;
import dev.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;
    @MockBean
    private NoteService noteService;
    @MockBean
    private PersonService personService;
    @MockBean
    private PersonDetailsService personDetailsService;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser
    @Test
    public void taskController_addTask() throws Exception {
        Person person = new Person(1L, "user", "password");
        Note note = new Note(1L, "Список задач 1", LocalDateTime.now(), person, Collections.emptyList());
        Task temp = new Task("Молоко", note);
        TaskDTO taskDTO = new TaskDTO(temp.getName(), 1L);
        String requestJson = objectMapper.writeValueAsString(taskDTO);

        Mockito.doNothing().when(taskService).addTask(temp);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void taskController_deleteTaskById() throws Exception {
        Mockito.doNothing().when(taskService).deleteTaskById(1L);

        mockMvc.perform(delete("/api/v1/tasks/1")).andExpect(status().isOk());

        Mockito.doThrow(new EntityNotFoundException("Задача с идентификатором 2 не найдена"))
                .when(taskService).deleteTaskById(2L);

        mockMvc.perform(delete("/api/v1/tasks/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Задача с идентификатором 2 не найдена")));
    }

    @Test
    @WithMockUser
    public void taskController_changeStatus() throws Exception {
        Mockito.doNothing().when(taskService).changeStatus(1L);

        mockMvc.perform(patch("/api/v1/tasks/1/changeStatus")).andExpect(status().isOk());

        Mockito.doThrow(new EntityNotFoundException("Задача с идентификатором 2 не найдена"))
                .when(taskService).changeStatus(2L);

        mockMvc.perform(patch("/api/v1/tasks/2/changeStatus"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Задача с идентификатором 2 не найдена")));
    }
}
