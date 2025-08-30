package com.example.shopzy.domain;

import java.time.Instant;

import com.example.shopzy.util.SecurityUtil;
import com.example.shopzy.util.constant.order.OrderPaymentMethodEnum;
import com.example.shopzy.util.constant.order.OrderPaymentStatusEnum;
import com.example.shopzy.util.constant.order.OrderStatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    5. Order (Đơn hàng)
    id : Mã đơn hàng
    userId : FK → User (người đặt)
    status : Trạng thái đơn hàng (PENDING, SHIPPING, COMPLETED, CANCELLED)
    paymentMethod : Phương thức thanh toán (COD, VNPAY…)
    paymentStatus : Trạng thái thanh toán (UNPAID, PAID)
    total : Tổng tiền
    shippingAddress : Địa chỉ giao hàng
    shippingPhone : Số điện thoại nhận hàng
    createdAt, createdBy, updatedAt, updatedBy

    =====================================================================
    status : Trạng thái đơn hàng (Đang chờ xử lý, ĐANG GIAO HÀNG, HOÀN THÀNH, ĐÃ HỦY) 
    COD (Cash on Delivery – thanh toán khi nhận hàng)
    VNPAY (Thanh toán qua cổng VNPAY)
    UNPAID – Thanh toán thất bại hoặc chưa thanh toán.
    PAID – Thanh toán thành công.

 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status = OrderStatusEnum.PENDING; // mặc định đang xử lý

    @Enumerated(EnumType.STRING)
    private OrderPaymentMethodEnum paymentMethod = OrderPaymentMethodEnum.COD; // mặc định thanh toán khi nhận hàng

    // mặc định chưa thanh toán hoặc thanh toán thất bại
    @Enumerated(EnumType.STRING)
    private OrderPaymentStatusEnum paymentStatus = OrderPaymentStatusEnum.UNPAID;

    private double total; // tổng tiền
    private String shippingAddress;
    private String shippingPhone;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    // dùng để cập nhật người tạo ra người dùng
    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.createdAt = Instant.now(); // tạo ra lúc
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.updatedAt = Instant.now(); // tạo ra lúc
    }
}
