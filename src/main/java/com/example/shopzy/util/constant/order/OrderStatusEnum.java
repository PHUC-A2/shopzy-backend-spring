package com.example.shopzy.util.constant.order;

/*
    status : Trạng thái đơn hàng (PENDING, SHIPPING, COMPLETED, CANCELLED)
    trạng thái : Trạng thái đơn hàng (Đang chờ xử lý, ĐANG GIAO HÀNG, HOÀN THÀNH, ĐÃ HỦY) 
    Phương thức thanh toán : Phương thức thanh toán (COD, VNPAY…) 
    Trạng thái thanh toán : Trạng thái thanh toán (UNPAID, PAID)
 */
public enum OrderStatusEnum {
    PENDING, SHIPPING, COMPLETED, CANCELLED
}
