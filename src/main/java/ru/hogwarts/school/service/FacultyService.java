package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Service
public class FacultyService {
    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Method createFaculty was invoked with parameters {}" , faculty);
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> findFaculty(Long id) {
        logger.info("Method findFaculty was invoked with parameters {}" , id);
        return facultyRepository.findById(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Method editFaculty was invoked with parameters {}" , faculty);
        if (facultyRepository.existsById(faculty.getId()))
            return facultyRepository.save(faculty);
        return null;
    }

    public void deleteFaculty(Long id) {
        logger.info("Method deleteFaculty was invoked with parameters {}" , id);
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAll() {
        logger.info("Method getAll was invoked");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getByName(String name) {
        logger.info("Method getByName was invoked with parameters {}" , name);
        return facultyRepository.findAllByNameContainsIgnoreCase(name);
    }

    public Collection<Faculty> getByColor(String color) {
        logger.info("Method getByColor was invoked with parameters {}" , color);
        return facultyRepository.findAllByColorContainsIgnoreCase(color);
    }

    public Collection<Student> getStudentsFromFaculty(Long id) {
        logger.info("Method getStudentsFromFaculty was invoked with parameters {}" , id);
        return facultyRepository.findById(id).map(Faculty::getStudents).orElseThrow(RuntimeException::new);
    }

    public String getLongestName() {
        return facultyRepository.findAll()
                .parallelStream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .orElseThrow(RuntimeException::new);
    }
}
