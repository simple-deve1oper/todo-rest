package dev.todo.service;

import dev.todo.entity.Note;
import dev.todo.entity.Task;
import dev.todo.exception.EntityNotFoundException;
import dev.todo.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;

    @Test
    public void taskService_getTaskById() {
        Note note = new Note(1L, "Список задач", LocalDateTime.now(), Collections.emptyList());
        Task task = new Task(1L, "Молоко", false, note);

        Mockito.when(taskRepository.findById(1L))
                .thenReturn(Optional.ofNullable(task));
        Mockito.when(taskRepository.findById(2L))
                .thenThrow(new EntityNotFoundException("Задача с идентификатором 2 не найдена"));

        Task taskFromService = taskService.getTaskById(1L);
        Assertions.assertTrue(taskFromService instanceof Task);
        Assertions.assertNotNull(taskFromService);
        Assertions.assertEquals(1L, taskFromService.getId());
        Assertions.assertEquals("Молоко", taskFromService.getName());
        Assertions.assertFalse(taskFromService.getDone());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class, () -> taskService.getTaskById(2L)
        );
        String expectedMessage = "Задача с идентификатором 2 не найдена";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(taskRepository, Mockito.times(2)).findById(Mockito.any(Long.class));
    }

    @Test
    public void taskService_addTask() {
        Note note = new Note(36L, "Список задач", LocalDateTime.now(), Collections.emptyList());
        Task temp = new Task("Молоко", note);
        Task task = new Task(55L, "Молоко", false, note);
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
        taskService.addTask(temp);

        Mockito.when(taskRepository.findById(2L))
                .thenThrow(new EntityNotFoundException("Задача с идентификатором 2 не найдена"));
        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class, () -> taskService.getTaskById(2L)
        );
        String expectedMessage = "Задача с идентификатором 2 не найдена";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Mockito.verify(taskRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any(Task.class));
    }

    @Test
    public void taskService_deleteTaskById() {
        Note note = new Note(1L, "Список задач", LocalDateTime.now(), Collections.emptyList());
        Task task = new Task(1L, "Молоко", false, note);
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(task));
        Mockito.doNothing().when(taskRepository).delete(task);
        taskService.deleteTaskById(task.getId());

        Mockito.when(taskRepository.findById(3L))
                .thenThrow(new EntityNotFoundException("Задача с идентификатором 3 не найдена"));

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class, () -> taskService.getTaskById(3L)
        );
        String expectedMessage = "Задача с идентификатором 3 не найдена";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(taskRepository, Mockito.times(2)).findById(Mockito.any(Long.class));
        Mockito.verify(taskRepository, Mockito.times(1)).delete(Mockito.any(Task.class));
    }

    @Test
    public void taskService_changeStatus() {
        Note note = new Note(1L, "Список задач", LocalDateTime.now(), Collections.emptyList());
        Task task = new Task(1L, "Молоко", false, note);
        Task task2 = new Task(1L, "Молоко", true, note);

        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(task));
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task2);

        taskService.changeStatus(1L);

        Mockito.when(taskRepository.findById(2L))
                .thenThrow(new EntityNotFoundException("Задача с идентификатором 2 не найдена"));
        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class, () -> taskService.getTaskById(2L)
        );
        String expectedMessage = "Задача с идентификатором 2 не найдена";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Mockito.verify(taskRepository, Mockito.times(2)).findById(Mockito.any(Long.class));
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any(Task.class));
    }
}
