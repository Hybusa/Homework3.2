package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> studentMap = new HashMap<>();

    private Long counter = 0L;

    public Student createStudent(Student student) {
        student.setId(counter);
        studentMap.put(counter++, student);
        return student;
    }

    public Student findStudent(Long id) {
        return studentMap.get(id);
    }

    public Student editStudent(Student student) {
        if (studentMap.containsKey(student.getId())) {
            studentMap.put(student.getId(), student);
            return student;
        }
        return null;
    }

    public Student deleteStudent(Long id) {
        return studentMap.remove(id);
    }

    public Collection<Student> getAll() {
        return studentMap.values();
    }

    public Collection<Student> getFilteredByAge(int age) {
       return studentMap.values().stream()
               .filter(s -> s.getAge() == age).collect(Collectors.toList());
    }
}
