package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> findStudent(Long id) {
        return studentRepository.findById(id);
    }

    public Student editStudent(Student student) {
        if(studentRepository.existsById(student.getId()))
            return studentRepository.save(student);
        return null;
    }
    public Collection<Student> getAllByName(String name){
        return studentRepository.findAllByNameContainsIgnoreCase(name);
    }

    public Collection<Student> getAllByAgeBetween(int min, int max){
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> getAllByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    public Faculty getFaculty(Long id) {
        Optional<Student> tmp = studentRepository.findById(id);

        return tmp.map(Student::getFaculty).orElse(null);
    }
}
