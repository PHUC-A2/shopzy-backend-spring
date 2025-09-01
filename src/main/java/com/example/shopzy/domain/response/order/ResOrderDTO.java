package com.example.shopzy.domain.response.order;

import java.time.Instant;

import com.example.shopzy.util.constant.order.OrderPaymentMethodEnum;
import com.example.shopzy.util.constant.order.OrderPaymentStatusEnum;
import com.example.shopzy.util.constant.order.OrderStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResOrderDTO {
    private Long id;
    private OrderStatusEnum status = OrderStatusEnum.PENDING; // mặc định đang xử lý
    private OrderPaymentMethodEnum paymentMethod = OrderPaymentMethodEnum.COD; // mặc định thanh toán khi nhận hàng
    private OrderPaymentStatusEnum paymentStatus = OrderPaymentStatusEnum.UNPAID;
    private double total; // tổng tiền
    private String shippingAddress;
    private String shippingPhone;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private OrderUser user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderUser {
        private Long id;
        private String name;
        private String fullName;
        private String email;
        private String phoneNumber;
    }
}
