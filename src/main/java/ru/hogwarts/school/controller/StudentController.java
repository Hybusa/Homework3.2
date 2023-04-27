package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("students")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.editStudent(student));
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) Integer age) {
        if (name != null && !name.isBlank())
            return ResponseEntity.ok(studentService.getAllByName(name));
        if (age != null)
            return ResponseEntity.ok(studentService.getAllByAge(age));
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        return studentService.findStudent(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("{id}/faculty")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getFaculty(id));
    }


    @GetMapping("filter/{min}/{max}")
    public ResponseEntity<Collection<Student>> getAllFilteredByAge(@PathVariable int min, @PathVariable int max) {
        return ResponseEntity.ok(studentService.getAllByAgeBetween(min, max));
    }

    @GetMapping("amount")
    public ResponseEntity<Integer> getAmountOfStudents(){
        return ResponseEntity.ok().body(studentRepository.numberOfStudents());
    }

    @GetMapping("age/avarage")
    public ResponseEntity<Integer> avarageStudentAge(){
        return ResponseEntity.ok().body(studentRepository.avarageStudentAge());
    }
    @GetMapping("getLast")
    public ResponseEntity<List<Student>> getLastFive(){
        return ResponseEntity.ok(studentRepository.getLastFiveStudents());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

}
