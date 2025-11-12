package com.example.shopzy.domain.request.cartitem;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateQuantityDTO {
    private Long productId; // thêm
    private int quantity; // delta hoặc số lượng muốn set
}
