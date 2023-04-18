package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {StudentService.class})
@ExtendWith(SpringExtension.class)
class StudentServiceTest {
    @Autowired
    private StudentService studentService;

    @BeforeEach
    public void clear() {
        studentService = new StudentService();
    }

    /**
     * Method under test: {@link StudentService#createStudent(Student)}
     */
    @Test
    void createStudent_CorrectParams_ReturnsCorrectStudentAndActuallyAdds() {

        Student student = new Student("Harry Potter", 13);

        Collection<Student> studentCollection = studentService.getAll();

        assertEquals(student, this.studentService.createStudent(student));
        assertEquals(1, studentCollection.size());
        assertTrue(studentCollection.contains(student));
    }

    /**
     * Method under test: {@link StudentService#findStudent(Long)}
     */
    @Test
    void findStudent_IncorrectRequest_ReturnsNull() {
        Student student = new Student("Harry", 13);
        Long idUnderTest = 2L;

        studentService.createStudent(student);

        assertNull(this.studentService.findStudent(idUnderTest));
    }

    @Test
    void findStudent_CorrectParams_ReturnsCorrectStudent() {
        Student student = new Student("Harry", 13);
        Student student1 = new Student("Hermione", 15);
        Student student2 = new Student("Ron", 14);


        studentService.createStudent(student);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        Long idUnderTest = 2L;

        assertEquals(student2, this.studentService.findStudent(idUnderTest));
    }

    /**
     * Method under test: {@link StudentService#editStudent(Student)}
     */
    @Test
    void editStudent_IncorrectParams_ReturnsNull() {
        Student student = new Student("Harry", 13);

        studentService.createStudent(student);

        assertNull(this.studentService.editStudent(new Student("Ron", 14)));
    }

    @Test
    void editStudent_CorrectParams_ReturnsCorrectStudentAndActuallyChanges() {
        Student student = new Student("Harry", 13);
        Student student1 = new Student("Ron", 14);

        studentService.createStudent(student);
        Student expectedStudent = studentService.createStudent(student1);
        expectedStudent.setAge(15);
        expectedStudent.setName("Draco");

        assertEquals(expectedStudent, this.studentService.editStudent(expectedStudent));
        assertTrue(this.studentService.getAll().contains(expectedStudent));
    }

    /**
     * Method under test: {@link StudentService#deleteStudent(Long)}
     */

    @Test
    void deleteStudent_IncorrectParams_ReturnsNull() {
        Student student = new Student("Harry", 13);
        Student student1 = new Student("Ron", 14);
        Long idUnderTest = 3L;

        studentService.createStudent(student);
        studentService.createStudent(student1);

        assertNull(this.studentService.deleteStudent(idUnderTest));
    }

    @Test
    void deleteStudent_CorrectParams_ReturnsStudentAndActuallyDeletes() {
        Student student = new Student("Harry", 13);
        Student student1 = new Student("Ron", 14);

        studentService.createStudent(student);
        Student expectedStudent = studentService.createStudent(student1);

        assertEquals(expectedStudent, this.studentService.deleteStudent(expectedStudent.getId()));
        assertFalse(this.studentService.getAll().contains(expectedStudent));
    }

    /**
     * Method under test: {@link StudentService#getAll()}
     */
    @Test
    void getAll_ReturnsAll() {
        Student student = new Student("Harry", 13);
        Student student1 = new Student("Hermione", 15);
        Student student2 = new Student("Ron", 14);


        studentService.createStudent(student);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        Collection<Student> expectedCollection = List.of(student, student1, student2);
        Collection<Student> actualCollection = this.studentService.getAll();

        assertEquals(expectedCollection.size(), actualCollection.size());
        assertTrue(expectedCollection.containsAll(actualCollection));
    }

    /**
     * Method under test: {@link StudentService#getFilteredByAge(int)}
     */
    @Test
    void getFilteredByAge_CorrectParam_ReturnsCorrectCollection() {
        Student student = new Student("Harry", 13);
        Student student1 = new Student("Hermione", 15);
        Student student2 = new Student("Ron", 14);
        Student student3 = new Student("Draco", 15);
        int ageUnderTest = 15;

        studentService.createStudent(student);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        studentService.createStudent(student3);
        Collection<Student> expectedCollection = List.of(student1, student3);
        Collection<Student> actualCollection = this.studentService.getFilteredByAge(ageUnderTest);

        assertEquals(expectedCollection.size(),actualCollection.size());
        assertTrue(expectedCollection.containsAll(actualCollection));
    }
}

