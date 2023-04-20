package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ContextConfiguration(classes = {StudentService.class})
@ExtendWith({SpringExtension.class})
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    /**
     * Method under test: {@link StudentService#createStudent(Student)}
     */
    @Test
    void createStudent_CorrectParams_ReturnsCorrectStudent() {

        Student student = new Student("Harry Potter", 13);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentRepository.findAll()).thenReturn(List.of(student));

        Collection<Student> studentCollection = studentService.getAll();

        assertEquals(student, studentService.createStudent(student));
        assertEquals(1, studentCollection.size());
        assertTrue(studentCollection.contains(student));
        Mockito.verify(studentRepository).save(Mockito.any());
    }

    /**
     * Method under test: {@link StudentService#findStudent(Long)}
     */

    @Test
    void findStudent_CorrectParams_ReturnsCorrectStudent() {
        Student student = new Student("Harry", 13);
        Long idUnderTest = 2L;

        when(studentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(student));

        assertEquals(Optional.of(student), this.studentService.findStudent(idUnderTest));
        Mockito.verify(studentRepository).findById(Mockito.any());
    }


    /**
     * Method under test: {@link StudentService#editStudent(Student)}
     */
    @Test
    void editStudent_IncorrectParams_ReturnsNull() {
        Student testStudent = new Student("Ron", 14);
        testStudent.setId(2L);

        when(studentRepository.existsById(1L)).thenReturn(false);

        assertNull(this.studentService.editStudent(testStudent));

        verify(studentRepository,never()).save(Mockito.any(Student.class));
        verify(studentRepository).existsById(Mockito.any());
    }

    @Test
    void editStudent_CorrectParams_ReturnsCorrectStudentAndActuallyChanges() {
        Student student = new Student("Harry", 13);

        Student expectedStudent = new Student();
        expectedStudent.setId(2L);
        expectedStudent.setAge(15);
        expectedStudent.setName("Draco");
        when(studentRepository.findAll()).thenReturn(List.of(student,expectedStudent));
        when(studentRepository.existsById(any())).thenReturn(true);
        when(studentRepository.save(any())).thenReturn(expectedStudent);

        assertEquals(expectedStudent, studentService.editStudent(expectedStudent));
        assertTrue(this.studentService.getAll().contains(expectedStudent));
        verify(studentRepository).existsById(any());
        verify(studentRepository).save(any());
    }

    /**
     * Method under test: {@link StudentService#deleteStudent(Long)}
     */

    @Test
    void deleteStudent_IncorrectParams_ReturnsNull() {

        Long idUnderTest = 3L;

        studentService.deleteStudent(idUnderTest);

        verify(studentRepository).deleteById(any());
    }

    @Test
    void deleteStudent_CorrectParams_ReturnsStudentAndActuallyDeletes() {
        Student expectedStudent = new Student("Harry", 13);
        expectedStudent.setId(2L);
        studentRepository.deleteById(expectedStudent.getId());

        verify(studentRepository).deleteById(any());
    }

    /**
     * Method under test: {@link StudentService#getAll()}
     */
    @Test
    void getAll_ReturnsAll() {
        Student student = new Student("Harry", 13);
        Student student1 = new Student("Hermione", 15);
        Student student2 = new Student("Ron", 14);

        List<Student> expectedCollection = List.of(student, student1, student2);
        when(studentRepository.findAll()).thenReturn(expectedCollection);
        List<Student> actualCollection = studentService.getAll();


        assertEquals(expectedCollection.size(), actualCollection.size());
        assertTrue(expectedCollection.containsAll(actualCollection));

        verify(studentRepository).findAll();
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

        Collection<Student> expectedCollection = List.of(student1, student3);
        when(studentRepository.findAll()).thenReturn(List.of(student,student1,student2,student3));
        Collection<Student> actualCollection = this.studentService.getFilteredByAge(ageUnderTest);

        assertEquals(expectedCollection.size(), actualCollection.size());
        assertTrue(expectedCollection.containsAll(actualCollection));
    }
}


