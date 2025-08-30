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

import com.example.shopzy.domain.CartItem;
import com.example.shopzy.domain.response.ResultPaginationDTO;
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
    public ResponseEntity<CartItem> createCartItem(@RequestBody CartItem cartItem) {
        CartItem created = this.cartItemService.createCartItem(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/cart-items")
    @ApiMessage("get all cart items")
    public ResponseEntity<ResultPaginationDTO> getAllCartItems(@Filter Specification<CartItem> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.cartItemService.getAllCartItems(spec, pageable));
    }

    @GetMapping("/cart-items/{id}")
    @ApiMessage("Get cart item by id")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(this.cartItemService.getCartItemById(id));
    }

    @PutMapping("/cart-items/{id}")
    @ApiMessage("Update cart item")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem) throws IdInvalidException {
        CartItem update = this.cartItemService.updateCartItem(cartItem);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/cart-items/{id}")
    @ApiMessage("Delete cart item")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("id") Long id) throws IdInvalidException {
        this.cartItemService.deleteCartItem(id);
        return ResponseEntity.ok(null);
    }

}
