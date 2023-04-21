package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Optional<Faculty> tmp = facultyService.findFaculty(id);
        return tmp.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("{id}/students")
    public ResponseEntity<Collection<Student>> getFacultyStudents(@PathVariable Long id){
        Collection<Student> tmp = facultyService.getStudentsFromFaculty(id);
        if(tmp == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tmp);
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAll());
    }

    @GetMapping("filter/{color}")
    public ResponseEntity<Collection<Faculty>> getAllFilteredByAge(@PathVariable String color) {
        return ResponseEntity.ok(facultyService.getFilteredByColor(color));
    }

    @GetMapping("filter")
    public ResponseEntity<Collection<Faculty>> getAllFaculties(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) String color){
        if(name != null && !name.isBlank())
            return ResponseEntity.ok(facultyService.getByName(name));
        if(color != null && !color.isBlank())
            return ResponseEntity.ok(facultyService.getByColor(color));
        return ResponseEntity.ok(facultyService.getAll());
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editBook(@RequestBody Faculty faculty) {
        Faculty tmp = facultyService.editFaculty(faculty);
        if (tmp == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(tmp);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }
}
