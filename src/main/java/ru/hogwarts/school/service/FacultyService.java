package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty){
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> findFaculty(Long id){
        return facultyRepository.findById(id);
    }

    public Faculty editFaculty(Faculty faculty){
        if(facultyRepository.existsById(faculty.getId()))
            return facultyRepository.save(faculty);
        return null;
    }

    public void deleteFaculty(Long id){
        facultyRepository.deleteById(id);
    }

    public List<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public List<Faculty> getFilteredByColor(String color) {
        return facultyRepository.findAll().stream()
                .filter(f -> f.getColor().equals(color)).collect(Collectors.toList());
    }
}
