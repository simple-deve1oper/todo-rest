package dev.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс-сущность для описания записи
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;    // идентификатор
    @Column(name = "title")
    private String title;   // наименование
    @CreationTimestamp
    @Column(name = "date_time")
    private LocalDateTime dateTime; // дата и время

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @OneToMany(mappedBy = "note")
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<Task> tasks;   // список задач

    public Note(String title, Person person) {
        this.title = title;
        this.person = person;
    }
}
