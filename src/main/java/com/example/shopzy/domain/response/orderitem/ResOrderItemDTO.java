package com.example.shopzy.domain.response.orderitem;

import java.time.Instant;

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
    private Long orderId;
    private int quantity;
    private double unitPrice; // giá tại thời điểm bán

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    private OrderItemProduct product;

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
}
