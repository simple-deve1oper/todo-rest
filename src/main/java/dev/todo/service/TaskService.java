package dev.todo.service;

import dev.todo.entity.Task;
import dev.todo.exception.EntityNotFoundException;
import dev.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public void addTask(Task task) {
        taskRepository.save(task);
    }

    public void deleteTaskById(Long id) {
        Task task = getTaskById(id);

        taskRepository.delete(task);
    }

    public void changeStatus(Long id) {
        Task task = getTaskById(id);
        task.setDone(task.getDone() ? false : true);

        taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Задача с идентификатором %d не найдена", id)));
    }
}
