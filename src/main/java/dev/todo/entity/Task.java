package dev.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

/**
 * Класс-сущность для описания задачи
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;    // идентификатор
    @Column(name = "name")
    private String name;    // наименование
    @Column(name = "done")
    private Boolean done;   // статус выполнения задачи
    @ManyToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Note note;  // запись

    public Task(String name, Note note) {
        this.name = name;
        this.note = note;
        this.done = false;
    }
}
