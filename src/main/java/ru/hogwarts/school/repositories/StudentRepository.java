package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Collection<Student> findAllByNameContainsIgnoreCase(String name);
    Collection<Student> findAllByAgeBetween(int min, int max);
    Collection<Student> findAllByAge(int age);
}
