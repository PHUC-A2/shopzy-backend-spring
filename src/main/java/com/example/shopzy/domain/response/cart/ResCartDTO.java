package com.example.shopzy.domain.response.cart;

import java.time.Instant;

import com.example.shopzy.util.constant.cart.CartStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResCartDTO {
    private Long id;
    private CartStatusEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private CartUser user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartUser {
        private Long id;
        private String name;
        private String fullName;
        private String email;
        private String phoneNumber;
    }
}
