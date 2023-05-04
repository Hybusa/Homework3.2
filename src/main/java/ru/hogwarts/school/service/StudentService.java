package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentService {
    Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Method createStudent was invoked with parameters {}", student);
        return studentRepository.save(student);
    }

    public Optional<Student> findStudent(Long id) {
        logger.info("Method findStudent was invoked with parameters {}", id);
        return studentRepository.findById(id);
    }

    public Student editStudent(Student student) {
        logger.info("Method editStudent was invoked with parameters {}", student);
        if(studentRepository.existsById(student.getId()))
            return studentRepository.save(student);
        return null;
    }
    public Collection<Student> getAllByName(String name){
        logger.info("Method getAllByName was invoked with parameters {}", name);
        return studentRepository.findAllByNameContainsIgnoreCase(name);
    }

    public Collection<Student> getAllByAgeBetween(int min, int max){
        logger.info("Method getAllByAgeBetween was invoked with parameters min:{} , max:{}", min,max);
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public void deleteStudent(Long id) {
        logger.info("Method deleteStudent was invoked with parameters {}", id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAll() {
        logger.info("Method getAll was invoked");
        return studentRepository.findAll();
    }

    public Collection<Student> getAllByAge(int age) {
        logger.info("Method deleteStudent was invoked with parameters {}", age);
        return studentRepository.findAllByAge(age);
    }

    public Faculty getFaculty(Long id) {
        logger.info("Method getFaculty was invoked with parameters {}", id);
        return studentRepository.findById(id).map(Student::getFaculty).orElseThrow(RuntimeException::new);
    }
}
