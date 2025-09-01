package com.example.shopzy.domain.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqUpdateUserDTO {
    
    private Long id;

    @NotBlank(message = "name không được để trống")
    private String name;

    @NotBlank(message = "fullName không được để trống")
    private String fullName;

    @NotBlank(message = "phoneNumber không được để trống")
    private String phoneNumber;
}
