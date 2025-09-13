package com.example.shopzy.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopzy.domain.entity.CartItem;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.domain.response.cartitem.ResCartItemDTO;
import com.example.shopzy.service.CartItemService;
import com.example.shopzy.util.annotation.ApiMessage;
import com.example.shopzy.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1")
public class CartItemController {
    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/cart-items")
    @ApiMessage("Create cart item")
    public ResponseEntity<ResCartItemDTO> createCartItem(@RequestBody CartItem cartItem) throws IdInvalidException {
        CartItem created = this.cartItemService.createCartItem(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.cartItemService.convertToResCartItemDTO(created));
    }

    @GetMapping("/cart-items")
    @ApiMessage("get all cart items")
    public ResponseEntity<ResultPaginationDTO> getAllCartItems(@Filter Specification<CartItem> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.cartItemService.getAllCartItems(spec, pageable));
    }

    @GetMapping("/cart-items/{id}")
    @ApiMessage("Get cart item by id")
    public ResponseEntity<ResCartItemDTO> getCartItemById(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity
                .ok(this.cartItemService.convertToResCartItemDTO(this.cartItemService.getCartItemById(id)));
    }

    @PutMapping("/cart-items")
    @ApiMessage("Update cart item")
    public ResponseEntity<ResCartItemDTO> updateCartItem(@RequestBody CartItem cartItem) throws IdInvalidException {
        CartItem updated = this.cartItemService.updateCartItem(cartItem);
        return ResponseEntity.ok(this.cartItemService.convertToResCartItemDTO(updated));
    }

    @DeleteMapping("/cart-items/{id}")
    @ApiMessage("Delete cart item")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("id") Long id) throws IdInvalidException {
        this.cartItemService.deleteCartItem(id);
        return ResponseEntity.ok(null);
    }

}
