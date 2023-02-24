package dev.todo.controller;

import dev.todo.dto.PersonDTO;
import dev.todo.entity.Person;
import dev.todo.exception.EntityAlreadyExistsException;
import dev.todo.exception.EntityValidationException;
import dev.todo.service.PersonService;
import dev.todo.util.DataTransformation;
import dev.todo.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private PersonService personService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registrationPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = StringUtils.getErrorsFromValidation(bindingResult);

            throw new EntityValidationException(errors);
        }

        if (personService.existsByUsername(personDTO.getUsername())) {
            throw new EntityAlreadyExistsException(String.format("Пользователь с именем пользователя %s уже существует"));
        }
        Person person = DataTransformation.convertingPersonDataFromDtoToEntity(personDTO);
        String password = passwordEncoder.encode(person.getPassword());
        person.setPassword(password);
        personService.registerPerson(person);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
