package com.majlo.antares.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {
    private final String uploadDir;
    private final String baseUrl;

    public ImageService(@Value("${app.base-url}") String baseUrl,
                        @Value("${app.upload-dir:uploads/}") String uploadDir) {
        this.uploadDir = uploadDir;
        this.baseUrl = baseUrl;
    }

    public String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String randomFileName = UUID.randomUUID().toString() + fileExtension;

        Path filePath = uploadPath.resolve(randomFileName);
        file.transferTo(filePath);

        return "/api/images/files/" + randomFileName;
    }

    public byte[] getImage(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filename);
        }
        return Files.readAllBytes(filePath);
    }
}
