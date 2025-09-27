package com.example.shopzy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.shopzy.domain.entity.Cart;
import com.example.shopzy.util.constant.cart.CartStatusEnum;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
    List<Cart> findByUserId(Long userId);
    
    Optional<Cart> findByUserIdAndStatus(Long userId, CartStatusEnum status);
}
