package com.example.shopzy.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${shopzy.upload-file.base-uri}")
    private String baseURI;

    private void createUploadFolder(Path folderPath) {
        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot create upload folder: " + e.getMessage(), e);
        }
    }

    public String store(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty or missing");
        }

        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path uploadDir = Paths.get(baseURI, folder);
        createUploadFolder(uploadDir);

        Path filePath = uploadDir.resolve(finalName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return finalName;
    }
}
