package dev.todo.service;

import dev.todo.entity.Person;
import dev.todo.exception.EntityAlreadyExistsException;
import dev.todo.exception.EntityNotFoundException;
import dev.todo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public boolean existsByUsername(String username) {
        return personRepository.existsByUsername(username);
    }

    public void registerPerson(Person person) {
        var username = person.getUsername();
        if (personRepository.existsByUsername(username)) {
            throw new EntityAlreadyExistsException(String.format("Пользователь с именем пользователя %s уже существует", username));
        }

        personRepository.save(person);
    }

    public Person getPersonByUsername(String username) {
        return personRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с именем пользователя %s не найден", username)));
    }
}
