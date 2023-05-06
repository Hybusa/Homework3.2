package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Logger logger = LoggerFactory.getLogger(AvatarService.class);
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
        if (studentRepository.existsById(student.getId()))
            return studentRepository.save(student);
        return null;
    }

    public Collection<Student> getAllByName(String name) {
        logger.info("Method getAllByName was invoked with parameters {}", name);
        return studentRepository.findAllByNameContainsIgnoreCase(name);
    }

    public Collection<Student> getAllByAgeBetween(int min, int max) {
        logger.info("Method getAllByAgeBetween was invoked with parameters min:{} , max:{}", min, max);
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

    public Collection<String> getAllNamesStartingWith(String character) {
        return studentRepository.findAll()
                .parallelStream()
                .map(Student::getName)
                .filter(name -> name.startsWith(character))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getAvarageAge() {
        return studentRepository.findAll()
                .parallelStream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

    public void printAllInThreads() {
        List<Student> studentCollection = studentRepository.findAll();


        System.out.println("Original order: ");
        for (int i = 0; i < 6; i++) {
            System.out.println(studentCollection.get(i));
        }
        System.out.println();

        System.out.println("Main thread: " + studentCollection.get(0));
        System.out.println("Main thread: " + studentCollection.get(1));

        new Thread(() -> {
            System.out.println("Thread 1: " + studentCollection.get(2));
            System.out.println("Thread 1: " + studentCollection.get(3));
        }).start();

        new Thread(() -> {
            System.out.println("Thread 2: " + studentCollection.get(4));
            System.out.println("Thread 2: " + studentCollection.get(5));
        }).start();
    }

    public void printAllInSyncThreads() {

        List<Student> studentCollection = studentRepository.findAll();

        System.out.println("Original order: ");
        for (int i = 0; i < 6; i++) {
            System.out.println(studentCollection.get(i));
        }
        System.out.println();

        printForSyncThreads("Main thread: ", studentCollection.get(0).toString());
        printForSyncThreads("Main thread: ", studentCollection.get(1).toString());

        new Thread(() -> {

            printForSyncThreads("Thread 1: ", studentCollection.get(2).toString());
            printForSyncThreads("Thread 1: ", studentCollection.get(3).toString());

        }).start();

        new Thread(() -> {

            printForSyncThreads("Thread 2: ", studentCollection.get(4).toString());
            printForSyncThreads("Thread 2: ", studentCollection.get(5).toString());

        }).start();
    }

    private void printForSyncThreads(String string, String student) {
        synchronized (this) {
            System.out.println(string + student);
        }
    }
}
