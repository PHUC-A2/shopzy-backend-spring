package com.example.shopzy.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.shopzy.domain.response.file.ResUploadFileDTO;
import com.example.shopzy.service.FileService;
import com.example.shopzy.util.annotation.ApiMessage;
import com.example.shopzy.util.error.StorageException;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @Value("${shopzy.upload-file.base-uri}")
    private String baseURI;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @ApiMessage("Upload image file")
    public ResponseEntity<ResUploadFileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "folder", defaultValue = "products") String folder)
            throws IOException, StorageException {

        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty, please upload a valid file.");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp");
        boolean isValid = allowedExtensions.stream()
                .anyMatch(ext -> fileName.toLowerCase().endsWith("." + ext));

        if (!isValid) {
            throw new StorageException("Invalid file format. Only " + allowedExtensions + " allowed.");
        }

        // 🔹 Lưu file vật lý
        String uploadFileName = fileService.store(file, folder);

        // 🔹 Tạo URL static (dùng luôn được)
        // VD: http://localhost:8080/storage/products/173019231231-cat.png
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/storage/")
                .path(folder + "/")
                .path(uploadFileName)
                .toUriString();

        // 🔹 Trả về JSON
        ResUploadFileDTO response = new ResUploadFileDTO(uploadFileName, Instant.now(), imageUrl);
        return ResponseEntity.ok(response);
    }
}
