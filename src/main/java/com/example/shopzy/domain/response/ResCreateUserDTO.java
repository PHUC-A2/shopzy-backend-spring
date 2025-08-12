package com.example.shopzy.domain.response;

import java.time.Instant;

import com.example.shopzy.util.constant.UserStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResCreateUserDTO {

    private Long id;
    private String name;
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private UserStatusEnum status = UserStatusEnum.ACTIVE; // mặc định cho user đang hoạt động
    private Instant createdAt;
}
