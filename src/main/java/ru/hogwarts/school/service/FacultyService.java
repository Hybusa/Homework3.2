package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();
    private Long counter = 0L;

    public Faculty createFaculty(Faculty faculty){
        faculty.setId(counter);
        facultyMap.put(counter++, faculty);
        return faculty;
    }

    public Faculty findFaculty(Long id){
        return facultyMap.get(id);
    }

    public Faculty editFaculty(Faculty faculty){
        if (facultyMap.containsKey(faculty.getId())) {
            facultyMap.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    public Faculty deleteFaculty(Long id){
        return facultyMap.remove(id);
    }

    public Collection<Faculty> getAll() {
        return facultyMap.values();
    }

    public Collection<Faculty> getFilteredByColor(String color) {
        return facultyMap.values().stream()
                .filter(f -> f.getColor().equals(color)).collect(Collectors.toList());
    }
}
