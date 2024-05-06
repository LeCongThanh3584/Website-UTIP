package com.example.demo.Utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

public class FileUploadUtil {
    public static void uploadImage(String uploadDir, String imageName, MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = image.getInputStream()) {
            Path imagePath = uploadPath.resolve(imageName);
            Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + imageName);
        }
    }

    public static void deleteImage(String imageDir) throws IOException {
        Path imageDirPath = Paths.get(imageDir);

        if(Files.exists(imageDirPath)) {
            Files.walk(imageDirPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        else throw new IOException("Đường dẫn xoá ảnh không tồn tại");
    }
}
