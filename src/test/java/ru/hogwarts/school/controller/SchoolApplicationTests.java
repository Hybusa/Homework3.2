package ru.hogwarts.school.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class FacultyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;
    @MockBean
    private StudentController studentController;
    @MockBean
    private AvatarController avatarController;

    Long facultyId;
    String facultyName;
    String facultyColor;



    JSONObject facultyObject;
    Faculty faculty;

    @BeforeEach
    void setUp() throws JSONException {
        facultyId = 1L;
        facultyName = "Gryffindor";
        facultyColor = "Red";

        facultyObject = new JSONObject();
        facultyObject.put("name", facultyName);
        facultyObject.put("color", facultyColor);

        faculty = new Faculty(facultyName, facultyColor);
        faculty.setId(facultyId);
    }

    @Test
    void saveFaculty_CorrectRequest_CorrectResponse() throws Exception {

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(facultyColor));
    }

    @Test
    void getFacultyById_CorrectRequest_CorrectResponse() throws Exception {

        when(facultyService.findFaculty(anyLong())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + facultyId)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(facultyColor));
    }

    @Test
    void getStudentsByFacultyId_CorrectRequest_CorrectResponse() throws Exception {


        final Long studentId = 2L;
        final String studentName = "Harry";
        final int studentAge = 13;

        Student testStudent = new Student(studentName, studentAge);
        testStudent.setId(studentId);


        when(facultyService.getStudentsFromFaculty(anyLong())).thenReturn(List.of(testStudent));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + facultyId+"/students")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(studentId))
                .andExpect(jsonPath("$[0].name").value(studentName))
                .andExpect(jsonPath("$[0].age").value(studentAge));
    }

    @Test
    void getAllFaculties_CorrectRequest_CorrectResponse() throws Exception {


        when(facultyService.getAll()).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(facultyId))
                .andExpect(jsonPath("$[0].name").value(facultyName))
                .andExpect(jsonPath("$[0].color").value(facultyColor));
    }

    @Test
    void getAllFacultiesFiltered_CorrectRequest_CorrectResponse() throws Exception {

        when(facultyService.getByName(anyString())).thenReturn(List.of(faculty));
        when(facultyService.getByColor(anyString())).thenReturn(List.of(faculty));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/filter?name=" + facultyName)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(facultyId))
                .andExpect(jsonPath("$[0].name").value(facultyName))
                .andExpect(jsonPath("$[0].color").value(facultyColor));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/filter?color=" + facultyName)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(facultyId))
                .andExpect(jsonPath("$[0].name").value(facultyName))
                .andExpect(jsonPath("$[0].color").value(facultyColor));
    }


    @Test
    void editFaculty_CorrectRequest_CorrectResponse() throws Exception {

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(facultyColor));
    }


    @Test
    void deleteFaculty_CorrectRequest_CorrectResponse() throws Exception {

        final Long facultyId = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + facultyId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}

@WebMvcTest
class StudentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;
    @MockBean
    private FacultyController facultyController;
    @MockBean
    private AvatarController avatarController;


    Long studentId;
    String studentName;
    int studentAge = 13;
    JSONObject studentObject;
    Student student;

    @BeforeEach
    void setup() throws JSONException {
        studentId = 1L;
        studentName = "Harry Potter";
        studentAge = 13;

        studentObject = new JSONObject();
        studentObject.put("name", studentName);
        studentObject.put("age", studentAge);

        student = new Student(studentName, studentAge);
        student.setId(studentId);
    }
    @Test
    void saveStudent_CorrectRequest_CorrectResponse() throws Exception {

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value(studentName))
                .andExpect(jsonPath("$.age").value(studentAge));
    }

    @Test
    void getStudentById_CorrectRequest_CorrectResponse() throws Exception {

        when(studentService.findStudent(anyLong())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/" + studentId)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value(studentName))
                .andExpect(jsonPath("$.age").value(studentAge));
    }

    @Test
    void getStudentsByFacultyId_CorrectRequest_CorrectResponse() throws Exception {

        final Long facultyId = 2L;
        final String facultyName = "Gryffindor";
        final String facultyColor = "Red";


        Faculty faculty = new Faculty(facultyName, facultyColor);
        faculty.setId(facultyId);

        when(studentService.getFaculty(anyLong())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/" + studentId + "/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(facultyColor));
    }

    @Test
    void getAllStudents_CorrectRequest_CorrectResponse() throws Exception {

        when(studentService.getAll()).thenReturn(List.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(studentId))
                .andExpect(jsonPath("$[0].name").value(studentName))
                .andExpect(jsonPath("$[0].age").value(studentAge));
    }

    @Test
    void getAllStudentsFiltered_CorrectRequest_CorrectResponse() throws Exception {

        when(studentService.getAllByName(anyString())).thenReturn(List.of(student));
        when(studentService.getAllByAge(anyInt())).thenReturn(List.of(student));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students?name=" + studentName)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(studentId))
                .andExpect(jsonPath("$[0].name").value(studentName))
                .andExpect(jsonPath("$[0].age").value(studentAge));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students?age=" + studentAge)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(studentId))
                .andExpect(jsonPath("$[0].name").value(studentName))
                .andExpect(jsonPath("$[0].age").value(studentAge));
    }


    @Test
    void editStudent_CorrectRequest_CorrectResponse() throws Exception {

        when(studentService.editStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value(studentName))
                .andExpect(jsonPath("$.age").value(studentAge));
    }


    @Test
    void deleteStudent_CorrectRequest_CorrectResponse() throws Exception {

        final Long facultyId = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/" + facultyId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}