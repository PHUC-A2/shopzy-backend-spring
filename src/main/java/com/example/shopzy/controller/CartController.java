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

import com.example.shopzy.domain.entity.Cart;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.domain.response.cart.ResCartDTO;
import com.example.shopzy.service.CartService;
import com.example.shopzy.util.annotation.ApiMessage;
import com.example.shopzy.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1/")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts")
    @ApiMessage("Create a cart")
    public ResponseEntity<ResCartDTO> createcart(@RequestBody Cart cartReq) throws IdInvalidException {
        Cart created = this.cartService.createCart(cartReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.cartService.convertToResCartDTO(created));
    }

    @GetMapping("/carts")
    @ApiMessage("Get all cart")
    public ResponseEntity<ResultPaginationDTO> getAllCarts(@Filter Specification<Cart> spec, Pageable pageable) {
        return ResponseEntity.ok(this.cartService.getAllCarts(spec, pageable));
    }

    @GetMapping("/carts/{id}")
    @ApiMessage("Get cart by id")
    public ResponseEntity<ResCartDTO> getcartById(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(this.cartService.convertToResCartDTO(this.cartService.getCartById(id)));
    }

    @PutMapping("/carts")
    @ApiMessage("Update a cart")
    public ResponseEntity<ResCartDTO> updatecart(@RequestBody Cart cartReq) throws IdInvalidException {
        Cart cart = this.cartService.updateCart(cartReq);
        return ResponseEntity.ok(this.cartService.convertToResCartDTO(cart));
    }

    @DeleteMapping("/carts/{id}")
    @ApiMessage("Delete a cart")
    public ResponseEntity<Void> deletecart(@PathVariable("id") Long id) throws IdInvalidException {
        this.cartService.deleteCart(id);
        return ResponseEntity.ok(null);
    }
}
