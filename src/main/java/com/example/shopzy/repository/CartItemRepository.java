package com.example.shopzy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.shopzy.domain.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>, JpaSpecificationExecutor<CartItem> {
    // check trùng cart + product
    boolean existsByCartIdAndProductId(Long cartId, Long productId);

    // check trùng nhưng bỏ qua item hiện tại (dùng khi update)
    boolean existsByCartIdAndProductIdAndIdNot(Long cartId, Long productId, Long id);
}
