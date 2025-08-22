package com.example.shopzy.domain.response;

import java.time.Instant;

import com.example.shopzy.util.constant.user.UserStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResUpdateUserDTO {
    
    private Long id;
    private String name;
    private String fullName;
    private String phoneNumber;
    private UserStatusEnum status = UserStatusEnum.ACTIVE; // mặc định cho user đang hoạt động
    private Instant updatedAt;
}
