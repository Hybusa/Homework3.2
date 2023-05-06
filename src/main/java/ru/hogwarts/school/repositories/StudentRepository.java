package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Collection<Student> findAllByNameContainsIgnoreCase(String name);
    Collection<Student> findAllByAgeBetween(int min, int max);
    Collection<Student> findAllByAge(int age);

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveStudents();

    @Query(value = "SELECT COUNT(*) FROM student",nativeQuery = true)
    Integer numberOfStudents();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Integer avarageStudentAge();
}
