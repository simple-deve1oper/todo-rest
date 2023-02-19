package dev.todo.controller;

import dev.todo.dto.NoteDTO;
import dev.todo.entity.Note;
import dev.todo.exception.EntityValidationException;
import dev.todo.service.NoteService;
import dev.todo.util.DataTransformation;
import dev.todo.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @GetMapping("/all")
    public ResponseEntity<List<NoteDTO>> getAllNotes() {
        List<Note> notes = noteService.getAllNotes();
        List<NoteDTO> notesDTO = DataTransformation.convertingListNotesFromEntityToDTO(notes);

        return ResponseEntity.ok(notesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable("id") Long id) {
        Note note = noteService.getNoteById(id);
        NoteDTO noteDTO = DataTransformation.convertingNoteDataFromEntityToDTO(note);

        return ResponseEntity.ok(noteDTO);
    }

    @PostMapping
    public ResponseEntity<NoteDTO> addNote(@RequestBody @Valid NoteDTO noteDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = StringUtils.getErrorsFromValidation(bindingResult);

            throw new EntityValidationException(errors);
        }

        var title = noteDTO.getTitle();
        Note note = new Note(title);
        note = noteService.addNote(note);
        NoteDTO newDTO = DataTransformation.convertingNoteDataFromEntityToDTO(note);

        return new ResponseEntity<>(newDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNoteTitle(@PathVariable("id") Long id, @RequestParam("title") String title) {
        Note note = noteService.updateNoteTitle(id, title);
        NoteDTO noteDTO = DataTransformation.convertingNoteDataFromEntityToDTO(note);

        return ResponseEntity.ok(noteDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteNoteById(@PathVariable("id") Long id) {
        noteService.deleteNoteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
