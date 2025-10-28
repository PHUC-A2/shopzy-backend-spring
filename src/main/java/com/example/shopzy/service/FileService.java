package com.example.shopzy.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${shopzy.upload-file.base-uri}")
    private String baseURI; // Ví dụ: D:/B/project_ca_nhan/web_tmdt/demo/img_upload/upload/

    // Tạo thư mục nếu chưa tồn tại
    private void createUploadFolder(Path folderPath) {
        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
                System.out.println(">>> Created folder: " + folderPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot create upload folder: " + e.getMessage(), e);
        }
    }

    // Lưu file
    public String store(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty or missing");
        }

        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        // Không dùng URI ở đây, dùng Path trực tiếp
        Path uploadDir = Paths.get(baseURI, folder);
        createUploadFolder(uploadDir); // đảm bảo thư mục tồn tại

        Path filePath = uploadDir.resolve(finalName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return finalName;
    }

    // Lấy kích thước file
    public long getFileLength(String fileName, String folder) {
        Path filePath = Paths.get(baseURI, folder, fileName);
        File file = filePath.toFile();

        if (!file.exists() || file.isDirectory()) {
            return 0;
        }
        return file.length();
    }

    // Lấy file để tải về
    public InputStreamResource getResource(String fileName, String folder) throws FileNotFoundException {
        Path filePath = Paths.get(baseURI, folder, fileName);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        return new InputStreamResource(new FileInputStream(file));
    }
}
