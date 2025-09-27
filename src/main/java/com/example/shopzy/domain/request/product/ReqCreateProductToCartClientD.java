package com.example.shopzy.domain.request.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateProductToCartClientD {
    private Long productId;
    private int quantity;
}
