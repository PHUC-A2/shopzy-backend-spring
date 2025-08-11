package com.example.shopzy.util.constant;

public enum UserStatusEnum {
    ACTIVE, // Hoạt động bình thường
    INACTIVE, // Ngưng hoạt động / bị vô hiệu hóa
    PENDING_VERIFICATION, // Chờ xác minh email/OTP
    BANNED, // Bị cấm
    DELETED // Đã xóa (hoặc lưu trữ)
}
