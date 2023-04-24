package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
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
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

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
        Optional<Student> optStudent = studentService.findStudent(id);
        if(optStudent.isEmpty())
            throw new RuntimeException("Not Found");
        
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
        return avatarRepository.findByStudentId(id).orElse(new Avatar());
    }

/*    private String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf('.')+1);
    }*/

/*    private byte[] generateImagePreview(Path filePath) throws IOException{
        try(InputStream is = Files.newInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth()/100);
            BufferedImage preview = new BufferedImage(100, height,image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }*/
}
