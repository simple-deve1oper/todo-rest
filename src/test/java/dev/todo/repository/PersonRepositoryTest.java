package dev.todo.repository;

import dev.todo.entity.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person("test", "test123");
    }

    @AfterEach
    void tearDown() {
        personRepository.deleteAll();
    }

    @Test
    void personRepository_existsUsername() {
        var username = person.getUsername();
        personRepository.save(person);

        boolean result = personRepository.existsByUsername(username);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void personRepository_notExistsUsername() {
        var username = person.getUsername();

        boolean result = personRepository.existsByUsername(username);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    void noteRepository_personFound() {
        var username = person.getUsername();
        personRepository.save(person);

        Optional<Person> optionalPerson = personRepository.findByUsername(username);

        Assertions.assertThat(optionalPerson.isPresent()).isTrue();
    }

    @Test
    void noteRepository_personNotFound() {
        var username = person.getUsername();

        Optional<Person> optionalPerson = personRepository.findByUsername(username);

        Assertions.assertThat(optionalPerson.isPresent()).isFalse();
    }
}
