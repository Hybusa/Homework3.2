package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class FacultyService {
    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.debug("Method createFaculty was invoked with parameters {}" , faculty);
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> findFaculty(Long id) {
        logger.debug("Method findFaculty was invoked with parameters {}" , id);
        return facultyRepository.findById(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.debug("Method editFaculty was invoked with parameters {}" , faculty);
        if (facultyRepository.existsById(faculty.getId()))
            return facultyRepository.save(faculty);
        return null;
    }

    public void deleteFaculty(Long id) {
        logger.debug("Method deleteFaculty was invoked with parameters {}" , id);
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAll() {
        logger.debug("Method getAll was invoked");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getByName(String name) {
        logger.debug("Method getByName was invoked with parameters {}" , name);
        return facultyRepository.findAllByNameContainsIgnoreCase(name);
    }

    public Collection<Faculty> getByColor(String color) {
        logger.debug("Method getByColor was invoked with parameters {}" , color);
        return facultyRepository.findAllByColorContainsIgnoreCase(color);
    }

    public Collection<Student> getStudentsFromFaculty(Long id) {
        logger.debug("Method getStudentsFromFaculty was invoked with parameters {}" , id);
        return facultyRepository.findById(id).map(Faculty::getStudents).orElseThrow(RuntimeException::new);
    }
}
