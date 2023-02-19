package dev.todo.service;

import dev.todo.entity.Note;
import dev.todo.exception.EntityAlreadyExistsException;
import dev.todo.exception.EntityNotFoundException;
import dev.todo.repository.NoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;
    @InjectMocks
    private NoteService noteService;

    @Test
    public void noteRepository_getAllNotes() {
        Mockito.when(noteRepository.findAll())
                .thenReturn(
                        Arrays.asList(
                                new Note(1L, "Список задач 1", LocalDateTime.now(), Collections.emptyList()),
                                new Note(2L, "Список задач 2", LocalDateTime.now(), Collections.emptyList())
                        )
                );

        List<Note> notes = noteService.getAllNotes();
        Assertions.assertEquals(2, notes.size());

        Mockito.verify(noteRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void noteRepository_getNoteById() {
        Note note1 = new Note(1L, "Список задач 1", LocalDateTime.now(), Collections.emptyList());

        Mockito.when(noteRepository.findById(1L))
                .thenReturn(Optional.ofNullable(note1));
        Mockito.when(noteRepository.findById(2L))
                        .thenThrow(new EntityNotFoundException("Запись с идентификатором 2 не найдена"));

        Note noteFromService = noteService.getNoteById(1L);

        Assertions.assertEquals(1L, noteFromService.getId());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class, () -> noteService.getNoteById(2L)
        );
        String expectedMessage = "Запись с идентификатором 2 не найдена";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(noteRepository, Mockito.times(2)).findById(Mockito.any(Long.class));
    }

    @Test
    public void noteRepository_addNote() {
        Note temp = new Note("Список задач 1");
        Note note1 = new Note(1L, "Список задач 1", LocalDateTime.now(), Collections.emptyList());

        Mockito.when(noteRepository.save(Mockito.any(Note.class)))
                .thenReturn(note1);

        Note noteFromService = noteService.addNote(temp);

        Assertions.assertTrue(noteFromService instanceof Note);
        Assertions.assertNotNull(noteFromService);
        Assertions.assertEquals(1L, noteFromService.getId());
        Assertions.assertEquals("Список задач 1", noteFromService.getTitle());

        Mockito.when(noteRepository.existsByTitle(temp.getTitle()))
                .thenThrow(new EntityAlreadyExistsException("Запись с наименованием 'Список задач 1' уже существует"));

        EntityAlreadyExistsException exception = Assertions.assertThrows(
                EntityAlreadyExistsException.class, () -> noteService.addNote(temp)
        );
        String expectedMessage = "Запись с наименованием 'Список задач 1' уже существует";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(noteRepository, Mockito.times(1)).save(Mockito.any(Note.class));
    }

    @Test
    public void noteRepository_updateNoteTitle() {
        Note temp = new Note(1L, "Список задач 1", LocalDateTime.now(), Collections.emptyList());
        Note note1 = new Note(1L, "Тест", LocalDateTime.now(), Collections.emptyList());

        Mockito.when(noteRepository.findById(1L)).thenReturn(Optional.ofNullable(temp));
        Mockito.when(noteRepository.save(Mockito.any(Note.class)))
                .thenReturn(note1);

        Note tempFromService = noteService.getNoteById(1L);

        Assertions.assertTrue(tempFromService instanceof Note);
        Assertions.assertNotNull(tempFromService);
        Assertions.assertEquals(1L, tempFromService.getId());
        Assertions.assertEquals("Список задач 1", tempFromService.getTitle());

        Note noteFromService = noteService.updateNoteTitle(1L, "Тест");

        Assertions.assertTrue(noteFromService instanceof Note);
        Assertions.assertNotNull(noteFromService);
        Assertions.assertEquals(1L, noteFromService.getId());
        Assertions.assertEquals("Тест", noteFromService.getTitle());

        Mockito.when(noteRepository.findById(2L))
                .thenThrow(new EntityNotFoundException("Запись с идентификатором 2 не найдена"));

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class, () -> noteService.getNoteById(2L)
        );
        String expectedMessage = "Запись с идентификатором 2 не найдена";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(noteRepository, Mockito.times(3)).findById(Mockito.any(Long.class));
        Mockito.verify(noteRepository, Mockito.times(1)).save(Mockito.any(Note.class));
    }

    @Test
    public void noteRepository_deleteNoteById() {
        Note note = new Note(1L, "Список задач", LocalDateTime.now(), Collections.emptyList());
        Mockito.when(noteRepository.findById(1L)).thenReturn(Optional.ofNullable(note));
        Mockito.doNothing().when(noteRepository).delete(note);
        noteService.deleteNoteById(note.getId());

        Mockito.when(noteRepository.findById(3L))
                .thenThrow(new EntityNotFoundException("Запись с идентификатором 3 не найдена"));

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class, () -> noteService.getNoteById(3L)
        );
        String expectedMessage = "Запись с идентификатором 3 не найдена";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(noteRepository, Mockito.times(2)).findById(Mockito.any(Long.class));
        Mockito.verify(noteRepository, Mockito.times(1)).delete(Mockito.any(Note.class));
    }
}
