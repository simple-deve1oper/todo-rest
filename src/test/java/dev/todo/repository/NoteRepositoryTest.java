package dev.todo.repository;

import dev.todo.entity.Note;
import dev.todo.entity.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
public class NoteRepositoryTest {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private PersonRepository personRepository;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person("test", "test123");
    }

    @AfterEach
    void tearDown() {
        noteRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    void noteRepository_existsTitle() {
        person = personRepository.save(person);
        String title = "Список задач";
        Note note = new Note(title, person);
        noteRepository.save(note);

        boolean result = noteRepository.existsByTitleAndPerson_Id(title, person.getId());

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void noteRepository_notExistsTitle() {
        person = personRepository.save(person);
        String title = "Список задач";

        boolean result = noteRepository.existsByTitleAndPerson_Id(title, person.getId());

        Assertions.assertThat(result).isFalse();
    }

    @Test
    void noteRepository_allNotesFound() {
        person = personRepository.save(person);
        Note note = new Note("Список задач", person);
        Note note2 = new Note("Список задач 2", person);
        Note note3 = new Note("Список задач 3", person);
        noteRepository.saveAll(Arrays.asList(note, note2, note3));

        List<Note> notes = noteRepository.findByPerson_Id(person.getId());
        Assertions.assertThat(notes).isNotNull();
        Assertions.assertThat(notes.size()).isEqualTo(3);
    }

    @Test
    void noteRepository_allNotesNotFound() {
        person = personRepository.save(person);

        List<Note> notes = noteRepository.findByPerson_Id(person.getId());
        Assertions.assertThat(notes).isNotNull();
        Assertions.assertThat(notes).isEmpty();
    }
}
