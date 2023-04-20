
package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {FacultyService.class})
@ExtendWith(SpringExtension.class)
class FacultyServiceTest {
    @Autowired
    private FacultyService facultyService;
    @MockBean
    FacultyRepository facultyRepository;


    /**
     * Method under test: {@link FacultyService#createFaculty(Faculty)}
     */

    @Test
    void createFaculty_CorrectParams_ReturnsCorrectFacultyAndActuallyAdds() {

        Faculty faculty = new Faculty("Gryffindor", "Red");
        when(facultyRepository.save(faculty)).thenReturn(faculty);
        when(facultyRepository.findAll()).thenReturn(List.of(faculty));

        Collection<Faculty> facultyCollection = facultyService.getAll();

        assertEquals(faculty, facultyService.createFaculty(faculty));
        assertEquals(1, facultyCollection.size());
        assertTrue(facultyCollection.contains(faculty));
        Mockito.verify(facultyRepository).save(Mockito.any());
    }


    /**
     * Method under test: {@link FacultyService#findFaculty(Long)}
     */


    @Test
    void findFaculty_CorrectParams_ReturnsCorrectFaculty() {
        Faculty faculty = new Faculty("Gryffindor", "Red");
        Long idUnderTest = 2L;

        when(facultyRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(faculty));

        assertEquals(Optional.of(faculty), this.facultyService.findFaculty(idUnderTest));
        Mockito.verify(facultyRepository).findById(Mockito.any());
    }


    /**
     * Method under test: {@link FacultyService#editFaculty(Faculty)}
     */

    @Test
    void editFaculty_IncorrectParams_ReturnsNull() {
        Faculty testFaculty = new Faculty("Gryffindor", "Red");
        testFaculty.setId(2L);

        when(facultyRepository.existsById(1L)).thenReturn(false);

        assertNull(this.facultyService.editFaculty(testFaculty));

        verify(facultyRepository,never()).save(Mockito.any(Faculty.class));
        verify(facultyRepository).existsById(Mockito.any());
    }

    @Test
    void editFaculty_CorrectParams_ReturnsCorrectFacultyAndActuallyChanges() {
        Faculty faculty = new Faculty("Gryffindor", "Red");

        Faculty expectedFaculty = new Faculty();
        expectedFaculty.setId(2L);
        expectedFaculty.setColor("Green");
        expectedFaculty.setName("Slytherin");
        when(facultyRepository.findAll()).thenReturn(List.of(faculty,expectedFaculty));
        when(facultyRepository.existsById(any())).thenReturn(true);
        when(facultyRepository.save(any())).thenReturn(expectedFaculty);

        assertEquals(expectedFaculty, facultyService.editFaculty(expectedFaculty));
        assertTrue(this.facultyService.getAll().contains(expectedFaculty));
        verify(facultyRepository).existsById(any());
        verify(facultyRepository).save(any());
    }


    /**
     * Method under test: {@link FacultyService#deleteFaculty(Long)}
     */


    @Test
    void deleteFaculty_IncorrectParams_ReturnsNull() {

        Long idUnderTest = 3L;

        facultyService.deleteFaculty(idUnderTest);

        verify(facultyRepository).deleteById(any());
    }

    @Test
    void deleteFaculty_CorrectParams_ReturnsFacultyAndActuallyDeletes() {
        Faculty expectedFaculty = new Faculty("Gryffindor", "Red");
        expectedFaculty.setId(2L);
        facultyRepository.deleteById(expectedFaculty.getId());

        verify(facultyRepository).deleteById(any());
    }


    /**
     * Method under test: {@link FacultyService#getAll()}
     */

    @Test
    void getAll_ReturnsAll() {
        Faculty faculty = new Faculty("Gryffindor", "Red");
        Faculty faculty1 = new Faculty("Ravenclaw", "Blue");
        Faculty faculty2 = new Faculty("Slytherin", "Green");

        List<Faculty> expectedCollection = List.of(faculty, faculty1, faculty2);
        when(facultyRepository.findAll()).thenReturn(expectedCollection);
        List<Faculty> actualCollection = facultyService.getAll();


        assertEquals(expectedCollection.size(), actualCollection.size());
        assertTrue(expectedCollection.containsAll(actualCollection));

        verify(facultyRepository).findAll();
    }

    /**
     * Method under test: {@link FacultyService#getFilteredByColor(String)}
     */

    @Test
    void getFilteredByColor_CorrectParam_ReturnsCorrectCollection() {
        Faculty faculty = new Faculty("Gryffindor", "Red");
        Faculty faculty1 = new Faculty("Ravenclaw", "Blue");
        Faculty faculty2 = new Faculty("Slytherin", "Green");
        Faculty faculty3 = new Faculty("Ravenclaw", "Blue");
        String colorUnderTest = "Blue";

        Collection<Faculty> expectedCollection = List.of(faculty1, faculty3);
        when(facultyRepository.findAll()).thenReturn(List.of(faculty,faculty1,faculty2,faculty3));
        Collection<Faculty> actualCollection = this.facultyService.getFilteredByColor(colorUnderTest);

        assertEquals(expectedCollection.size(), actualCollection.size());
        assertTrue(expectedCollection.containsAll(actualCollection));
    }
}
