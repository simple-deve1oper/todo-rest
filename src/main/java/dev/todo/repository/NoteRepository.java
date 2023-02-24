package dev.todo.repository;

import dev.todo.entity.Note;
import dev.todo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    boolean existsByTitleAndPerson_Id(String title, Long personId);
    List<Note> findByPerson_Id(Long personId);
}
