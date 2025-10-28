package com.example.shopzy.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.shopzy.domain.response.file.ResUploadFileDTO;
import com.example.shopzy.service.FileService;
import com.example.shopzy.util.annotation.ApiMessage;
import com.example.shopzy.util.error.StorageException;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;

    @Value("${shopzy.upload-file.base-uri}")
    private String baseURI;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "folder", required = false) String folder)
            throws IOException, StorageException {

        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty, please upload a file.");
        }

        if (folder == null || folder.isBlank()) {
            folder = "default";
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");

        boolean isValid = allowedExtensions.stream()
                .anyMatch(item -> fileName.toLowerCase().endsWith("." + item));
        if (!isValid) {
            throw new StorageException("Invalid file extension. Only allows: " + allowedExtensions);
        }

        // Lưu file
        String uploadFileName = this.fileService.store(file, folder);

        // Tạo URL động (không fix domain)
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files")
                .queryParam("fileName", uploadFileName)
                .queryParam("folder", folder)
                .toUriString();

        // Gửi lại JSON chứa URL
        ResUploadFileDTO res = new ResUploadFileDTO(uploadFileName, Instant.now(), fileUrl);
        return ResponseEntity.ok(res);
    }

    // Download file
    @GetMapping("/files")
    @ApiMessage("Download a file")
    public ResponseEntity<Resource> download(
            @RequestParam(name = "fileName") String fileName,
            @RequestParam(name = "folder") String folder)
            throws StorageException, FileNotFoundException {

        long fileLength = this.fileService.getFileLength(fileName, folder);

        if (fileLength == 0) {
            throw new StorageException("File có tên: " + fileName + " không tồn tại");
        }

        InputStreamResource resource = this.fileService.getResource(fileName, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
