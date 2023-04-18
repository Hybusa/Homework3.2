package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {FacultyService.class})
@ExtendWith(SpringExtension.class)
class FacultyServiceTest {
    @Autowired
    private FacultyService facultyService;

    @BeforeEach
    public void clear() {
        facultyService = new FacultyService();
    }

    /**
     * Method under test: {@link FacultyService#createFaculty(Faculty)}
     */
    @Test
    void createFaculty_CorrectParams_ReturnsCorrectFacultyAndActuallyAdds() {

        Faculty faculty = new Faculty("Gryffindor", "Red");

        Collection<Faculty> facultyCollection = facultyService.getAll();

        assertEquals(faculty, this.facultyService.createFaculty(faculty));
        assertEquals(1, facultyCollection.size());
        assertTrue(facultyCollection.contains(faculty));
    }

    /**
     * Method under test: {@link FacultyService#findFaculty(Long)}
     */
    @Test
    void findFaculty_IncorrectRequest_ReturnsNull() {
        Faculty faculty = new Faculty("Gryffindor", "Red");
        Long idUnderTest = 2L;

        facultyService.createFaculty(faculty);

        assertNull(this.facultyService.findFaculty(idUnderTest));
    }

    @Test
    void findFaculty_CorrectParams_ReturnsCorrectFaculty() {
        Faculty faculty =  new Faculty("Gryffindor", "Red");
        Faculty faculty1 = new Faculty("Hufflepuff", "Yellow");
        Faculty faculty2 = new Faculty("Slytherin", "Green");


        facultyService.createFaculty(faculty);
        facultyService.createFaculty(faculty1);
        facultyService.createFaculty(faculty2);
        Long idUnderTest = 2L;

        assertEquals(faculty2, this.facultyService.findFaculty(idUnderTest));
    }

    /**
     * Method under test: {@link FacultyService#editFaculty(Faculty)}
     */
    @Test
    void editFaculty_IncorrectParams_ReturnsNull() {
        Faculty faculty =  new Faculty("Gryffindor", "Red");

        facultyService.createFaculty(faculty);

        assertNull(this.facultyService.editFaculty(new Faculty("Hufflepuff", "Yellow")));
    }

    @Test
    void editFaculty_CorrectParams_ReturnsCorrectFacultyAndActuallyChanges() {
        Faculty faculty =  new Faculty("Gryffindor", "Red");
        Faculty faculty1 = new Faculty("Hufflepuff", "Yellow");

        facultyService.createFaculty(faculty);
        Faculty expectedFaculty = facultyService.createFaculty(faculty1);
        expectedFaculty.setColor("Green");
        expectedFaculty.setName("Slytherin");

        assertEquals(expectedFaculty, this.facultyService.editFaculty(expectedFaculty));
        assertTrue(this.facultyService.getAll().contains(expectedFaculty));
    }

    /**
     * Method under test: {@link FacultyService#deleteFaculty(Long)}
     */

    @Test
    void deleteFaculty_IncorrectParams_ReturnsNull() {
        Faculty faculty =  new Faculty("Gryffindor", "Red");
        Faculty faculty1 = new Faculty("Hufflepuff", "Yellow");
        Long idUnderTest = 3L;

        facultyService.createFaculty(faculty);
        facultyService.createFaculty(faculty1);

        assertNull(this.facultyService.deleteFaculty(idUnderTest));
    }

    @Test
    void deleteFaculty_CorrectParams_ReturnsFacultyAndActuallyDeletes() {
        Faculty faculty =  new Faculty("Gryffindor", "Red");
        Faculty faculty1 = new Faculty("Hufflepuff", "Yellow");

        facultyService.createFaculty(faculty);
        Faculty expectedFaculty = facultyService.createFaculty(faculty1);

        assertEquals(expectedFaculty, this.facultyService.deleteFaculty(expectedFaculty.getId()));
        assertFalse(this.facultyService.getAll().contains(expectedFaculty));
    }

    /**
     * Method under test: {@link FacultyService#getAll()}
     */
    @Test
    void getAll_ReturnsAll() {
        Faculty faculty =  new Faculty("Gryffindor", "Red");
        Faculty faculty1 = new Faculty("Hufflepuff", "Yellow");
        Faculty faculty2 = new Faculty("Slytherin", "Green");


        facultyService.createFaculty(faculty);
        facultyService.createFaculty(faculty1);
        facultyService.createFaculty(faculty2);
        Collection<Faculty> expectedCollection = List.of(faculty, faculty1, faculty2);
        Collection<Faculty> actualCollection = this.facultyService.getAll();

        assertEquals(expectedCollection.size(), actualCollection.size());
        assertTrue(expectedCollection.containsAll(actualCollection));
    }

    /**
     * Method under test: {@link FacultyService#getFilteredByColor(String)}
     */
    @Test
    void getFilteredByColor_CorrectParam_ReturnsCorrectCollection() {
        Faculty faculty =  new Faculty("Gryffindor", "Red");
        Faculty faculty1 = new Faculty("Hufflepuff", "Yellow");
        Faculty faculty2 = new Faculty("Slytherin", "Green");
        Faculty faculty3 = new Faculty("SlytherinAdditional", "Green");
        String colorUnderTest = "Green";

        facultyService.createFaculty(faculty);
        facultyService.createFaculty(faculty1);
        facultyService.createFaculty(faculty2);
        facultyService.createFaculty(faculty3);
        Collection<Faculty> expectedCollection = List.of(faculty2, faculty3);
        Collection<Faculty> actualCollection = this.facultyService.getFilteredByColor(colorUnderTest);

        assertEquals(expectedCollection.size(),actualCollection.size());
        assertTrue(expectedCollection.containsAll(actualCollection));
    }
}

