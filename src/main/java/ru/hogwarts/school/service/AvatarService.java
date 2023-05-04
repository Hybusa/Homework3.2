package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.components.ImageProcessor;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;


import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${student.avatar.dir.path}")
    private String avatarDir;

    private final StudentService studentService;
    private final AvatarRepository avatarRepository;
    private final ImageProcessor imageProcessor;



    public AvatarService(StudentService studentService, AvatarRepository avatarRepository, ImageProcessor imageProcessor) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
        this.imageProcessor = imageProcessor;
    }

    public void uploadAvatar(Long id, MultipartFile avatarFile) throws IOException {
        logger.info("Method uploadAvatar was invoked with a file {}", avatarFile.getName());

        Optional<Student> optStudent = studentService.findStudent(id);
        if(optStudent.isEmpty()) {
            logger.error("Not Found");
            throw new RuntimeException("Not Found");
        }
        
        Student student = optStudent.get();
        String fileName = avatarFile.getOriginalFilename();
        if(fileName == null)
            throw  new RuntimeException();
        
        Path filepath = Path.of(avatarDir,id + "." + imageProcessor.getExtension(fileName));
        Files.createDirectories(filepath.getParent());
        Files.deleteIfExists(filepath);

        try(InputStream is = avatarFile.getInputStream();
            OutputStream os = Files.newOutputStream(filepath, CREATE_NEW);
            BufferedInputStream bis = new BufferedInputStream(is,1024);
            BufferedOutputStream bos = new BufferedOutputStream(os,1024)
            ){
            bis.transferTo(bos);
        }

        Avatar studentAvatar = findStudentAvatar(id);
        studentAvatar.setStudent(student);
        studentAvatar.setFilePath(filepath.toString());
        studentAvatar.setFileSize(avatarFile.getSize());
        studentAvatar.setMediaType(avatarFile.getContentType());
        studentAvatar.setPreview(imageProcessor.generateImagePreview(filepath));

        avatarRepository.save(studentAvatar);
    }

    public Avatar findStudentAvatar(Long id) {
        logger.info("Method findStudentAvatar was invoked with parameters {}", id);

        return avatarRepository.findByStudentId(id).orElse(new Avatar());
    }

    public List<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize) {
        logger.info("Method findStudentAvatar was invoked with parameters pageNumber: {}, pageSize: {}", pageNumber, pageSize);

        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }


}
