package com.example.shopzy.domain.response.cartitem;

import com.example.shopzy.util.constant.product.ProductStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResCartItemClientDTO {
    
    private Long cartItemId;
    private Long productId;
    private String name;
    private String imageUrl;
    private double price;
    private int quantity;
    private String size;
    private String color;
    private ProductStatusEnum status;
    private double subtotal;
}
