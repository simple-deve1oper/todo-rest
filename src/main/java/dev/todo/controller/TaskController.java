package dev.todo.controller;

import dev.todo.dto.TaskDTO;
import dev.todo.entity.Note;
import dev.todo.entity.Task;
import dev.todo.exception.EntityValidationException;
import dev.todo.service.NoteService;
import dev.todo.service.TaskService;
import dev.todo.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity<HttpStatus> addTask(@RequestBody @Valid TaskDTO taskDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = StringUtils.getErrorsFromValidation(bindingResult);

            throw new EntityValidationException(errors);
        }

        Note note = noteService.getNoteById(taskDTO.getNoteId());
        var name = taskDTO.getName();
        Task task = new Task(name, note);
        taskService.addTask(task);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTaskById(@PathVariable("id") Long id) {
        taskService.deleteTaskById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/changeStatus")
    public ResponseEntity<HttpStatus> changeStatus(@PathVariable("id") Long id) {
        taskService.changeStatus(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
