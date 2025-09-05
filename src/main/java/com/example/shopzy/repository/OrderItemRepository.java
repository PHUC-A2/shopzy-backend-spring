package com.example.shopzy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.shopzy.domain.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {
    boolean existsByOrderIdAndProductId(Long orderId, Long productId);
    
    // method check trùng nhưng bỏ qua item đang update
    boolean existsByOrderIdAndProductIdAndIdNot(Long orderId, Long productId, Long id);
}
