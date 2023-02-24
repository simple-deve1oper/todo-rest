package dev.todo.service;

import dev.todo.entity.Note;
import dev.todo.exception.EntityAlreadyExistsException;
import dev.todo.exception.EntityNotFoundException;
import dev.todo.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Запись с идентификатором %d не найдена", id)));
    }

    public Note addNote(Note note) {
        var title = note.getTitle();
        var personId = note.getPerson().getId();
        if (noteRepository.existsByTitleAndPerson_Id(title, personId)) {
            throw new EntityAlreadyExistsException(String.format("Запись с наименованием '%s' уже существует", title));
        }

        return noteRepository.save(note);
    }

    public Note updateNoteTitle(Long id, String title) {
        Note note = getNoteById(id);
        note.setTitle(title);

        return noteRepository.save(note);
    }

    public void deleteNoteById(Long id) {
        Note note = getNoteById(id);

        noteRepository.delete(note);
    }

    public List<Note> getAllNotesByPersonId(Long personId) {
        return noteRepository.findByPerson_Id(personId);
    }
}
