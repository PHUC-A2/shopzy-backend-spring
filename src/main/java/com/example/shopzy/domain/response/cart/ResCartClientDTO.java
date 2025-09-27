package com.example.shopzy.domain.response.cart;

import java.util.List;

import com.example.shopzy.domain.response.cartitem.ResCartItemClientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResCartClientDTO {
    
    private Long cartId;
    private List<ResCartItemClientDTO> cartItems;
    private double totalPrice;
}
