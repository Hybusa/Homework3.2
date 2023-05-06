package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.components.ImageProcessor;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AvatarService.class})
@ExtendWith(SpringExtension.class)
class AvatarServiceTest {
    @Autowired
    private AvatarService avatarService;

    @MockBean
    private StudentService studentService;
    @MockBean
    private AvatarRepository avatarRepository;
    @MockBean
    private ImageProcessor imageProcessor;

    Student testStudent;
    MultipartFile mockAvatar;
    Avatar testAvatar;

    @BeforeEach
    void setUp() throws IOException {
        testStudent = new Student("Harry Potter", 13);
        testStudent.setId(3L);
        testStudent.setFaculty(new Faculty("Gryffindor", "Red"));

        mockAvatar = new MockMultipartFile("file", "avatars/Harry.jpg",
                "image/jpeg", "content data".getBytes());

        testAvatar = new Avatar();
        testAvatar.setMediaType(mockAvatar.getContentType());
        testAvatar.setFileSize(mockAvatar.getSize());
        testAvatar.setFilePath(mockAvatar.getOriginalFilename());
        testAvatar.setData(mockAvatar.getBytes());
        testAvatar.setStudent(testStudent);
    }

    /**
     * Method under test: {@link AvatarService#uploadAvatar(Long, MultipartFile)}
     */
    @Test
    void uploadAvatar_success() throws IOException {

        when(studentService.findStudent(anyLong())).thenReturn(Optional.of(testStudent));
        when(avatarRepository.findByStudentId(anyLong())).thenReturn(Optional.of(testAvatar));
        when(imageProcessor.generateImagePreview(any(Path.class))).thenReturn("content data".getBytes());
        when(imageProcessor.getExtension(any(String.class))).thenReturn("jpg");

        this.avatarService.uploadAvatar(anyLong(), mockAvatar);

        verify(studentService).findStudent(anyLong());
        verify(avatarRepository).save(any());
    }

    /**
     * Method under test: {@link AvatarService#getAllAvatars(Integer, Integer)}
     */
    @Test
    void testGetAllAvatars() {

        int pageNumber = 1;
        int pageSize = 2;

        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);

        Page<Avatar> avatarPage = Page.empty(pageRequest);

        when(avatarRepository.findAll(pageRequest)).thenReturn(avatarPage);

        avatarService.getAllAvatars(pageNumber,pageSize);

        verify(avatarRepository).findAll(any(PageRequest.class));
    }
}

