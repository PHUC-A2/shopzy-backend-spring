package com.example.shopzy.domain.response.file;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResUploadFileDTO {
    private String fileName;
    private Instant uploadedAt; // thời gian upload thành công
    private String url; // đường dẫn để React hiển thị ảnh
}
