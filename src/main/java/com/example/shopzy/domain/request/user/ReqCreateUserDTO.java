package com.example.shopzy.domain.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// request chỉ chứa các trường có NotBlank
public class ReqCreateUserDTO {

    @NotBlank(message = "name không được để trống")
    private String name;

    @NotBlank(message = "fullName không được để trống")
    private String fullName;

    @NotBlank(message = "email không được để trống")
    private String email;

    @NotBlank(message = "password không được để trống")
    private String password;

    @NotBlank(message = "phoneNumber không được để trống")
    private String phoneNumber;

}
