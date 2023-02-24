package dev.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Класс-сущность для описания пользователя
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "people")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "person")
    private List<Note> notes;

    public Person(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
