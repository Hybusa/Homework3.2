package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> findFaculty(Long id) {
        return facultyRepository.findById(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (facultyRepository.existsById(faculty.getId()))
            return facultyRepository.save(faculty);
        return null;
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFilteredByColor(String color) {
        return facultyRepository.findAll().stream()
                .filter(f -> f.getColor().equals(color)).collect(Collectors.toList());
    }

    public Collection<Faculty> getByName(String name) {
        return facultyRepository.findAllByNameContainsIgnoreCase(name);
    }

    public Collection<Faculty> getByColor(String color) {
        return facultyRepository.findAllByColorContainsIgnoreCase(color);
    }

    public Collection<Student> getStudentsFromFaculty(Long id) {
        Optional<Faculty> tmp = facultyRepository.findById(id);
        return tmp.map(Faculty::getStudents).orElse(null);
    }

}
