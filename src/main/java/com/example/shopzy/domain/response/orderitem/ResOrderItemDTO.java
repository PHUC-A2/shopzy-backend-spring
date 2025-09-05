package com.example.shopzy.domain.response.orderitem;

import java.time.Instant;

import com.example.shopzy.util.constant.order.OrderPaymentMethodEnum;
import com.example.shopzy.util.constant.order.OrderPaymentStatusEnum;
import com.example.shopzy.util.constant.order.OrderStatusEnum;
import com.example.shopzy.util.constant.product.ProductConditionEnum;
import com.example.shopzy.util.constant.product.ProductStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResOrderItemDTO {
    private Long id;
    // private Long orderId;
    private int quantity;
    private double unitPrice; // giá tại thời điểm bán

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    private OrderItemProduct product;
    private OrderItemOrder order;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemProduct {

        private Long id;
        private String name;
        private String description;
        private double price;
        private int stock; // Số lượng tồn kho
        private ProductStatusEnum status; // IN_STOCK / OUT_OF_STOCK
        private ProductConditionEnum productCondition; // mặc định là tình trạng hàng mới
        private String imageUrl;
        private String size;
        private String color;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemOrder {

        private Long id;
        private OrderStatusEnum status;
        private OrderPaymentMethodEnum paymentMethod;
        private OrderPaymentStatusEnum paymentStatus;
        private double total; // tổng tiền
        private String shippingAddress;
        private String shippingPhone;
        private Instant createdAt;
        private Instant updatedAt;
        private String createdBy;
        private String updatedBy;
        private OrderItemUser user;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemUser {
        private Long id;
        private String name;
        private String fullName;
        private String email;
        private String phoneNumber;
    }
}
