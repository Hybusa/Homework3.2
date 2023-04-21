package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }



    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Optional<Student> tmp = studentService.findStudent(id);
        return tmp.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("{id}/faculty")
    public ResponseEntity<Faculty> getFaculty (@PathVariable Long id){
        Faculty tmp = studentService.getFaculty(id);
        if(tmp == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tmp);
    }
    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) Integer age) {
        if(name != null && !name.isBlank())
            ResponseEntity.ok(studentService.getAllByName(name));
        if(age != null)
            ResponseEntity.ok(studentService.getAllByAge(age));
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("filter/{min}/{max}")
    public ResponseEntity<Collection<Student>> getAllFilteredByAge(@PathVariable int min, @PathVariable int max){
        return ResponseEntity.ok(studentService.getAllByAgeBetween(min, max));
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editBook(@RequestBody Student student) {
        Student tmp = studentService.editStudent(student);
        if (tmp == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(tmp);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

}
