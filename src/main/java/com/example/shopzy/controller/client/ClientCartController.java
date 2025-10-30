package com.example.shopzy.controller.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopzy.domain.entity.Cart;
import com.example.shopzy.domain.request.product.ReqCreateProductToCartClientD;
import com.example.shopzy.domain.response.cart.ResCartClientDTO;
import com.example.shopzy.service.CartService;
import com.example.shopzy.util.annotation.ApiMessage;
import com.example.shopzy.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/")
public class ClientCartController {

    private final CartService cartService;

    public ClientCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/client/carts/{id}")
    @ApiMessage("Get client cart")
    public ResponseEntity<ResCartClientDTO> getCartClient(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(this.cartService.convertToResCartClientDTO(id));
    }

    @GetMapping("/client/carts")
    @ApiMessage("Get or create current user cart")
    public ResponseEntity<ResCartClientDTO> getOrCreateCurrentCart() throws IdInvalidException {
        Cart cart = cartService.getOrCreateCartForCurrentUser();
        return ResponseEntity.ok(cartService.convertToResCartClientDTO(cart.getId()));
    }

    @PostMapping("/client/carts/add-products")
    @ApiMessage("Add product to current user cart")
    public ResponseEntity<ResCartClientDTO> addProductToCart(
            @RequestBody ReqCreateProductToCartClientD request) throws IdInvalidException {

        ResCartClientDTO cartDTO = cartService.addProductToCart(request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("/client/carts/checkout")
    @ApiMessage("Checkout current user cart")
    public ResponseEntity<ResCartClientDTO> checkoutCart() throws IdInvalidException {
        return ResponseEntity.ok(cartService.checkoutCart());
    }
}
