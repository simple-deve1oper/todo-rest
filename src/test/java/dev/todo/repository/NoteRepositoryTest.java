package dev.todo.repository;

import dev.todo.entity.Note;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class NoteRepositoryTest {
    @Autowired
    private NoteRepository noteRepository;

    @AfterEach
    void tearDown() {
        noteRepository.deleteAll();
    }

    @Test
    void setNoteRepository_existsTitle() {
        String title = "Список задач";
        Note note = new Note(title);
        noteRepository.save(note);

        boolean result = noteRepository.existsByTitle(title);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void setNoteRepository_notExistsTitle() {
        String title = "Список задач";

        boolean result = noteRepository.existsByTitle(title);

        Assertions.assertThat(result).isFalse();
    }
}
