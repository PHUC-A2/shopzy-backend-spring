package com.example.shopzy.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.shopzy.domain.CartItem;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.repository.CartItemRepository;
import com.example.shopzy.util.error.IdInvalidException;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public CartItem createCartItem(CartItem cartItem) {
        return this.cartItemRepository.save(cartItem);
    }

    public ResultPaginationDTO getAllCartItems(Specification<CartItem> spec, Pageable pageable ) {
         Page<CartItem> pageCartItem = this.cartItemRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageCartItem.getTotalPages());
        mt.setTotal(pageCartItem.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCartItem.getContent());

        return rs;
    }

    public CartItem getCartItemById(Long id) throws IdInvalidException {
        Optional<CartItem> cartItemOptional = this.cartItemRepository.findById(id);
        if (cartItemOptional.isPresent()) {
            return cartItemOptional.get();
        } else {
            throw new IdInvalidException("Không tìm thấy cart item với ID = " + id);
        }
    }

    public CartItem updateCartItem(CartItem cartItemReq) throws IdInvalidException {
        CartItem cartItem = this.getCartItemById(cartItemReq.getId());
        cartItem.setCartId(cartItemReq.getCartId());
        cartItem.setProductId(cartItemReq.getProductId());
        cartItemReq.setQuantity(cartItemReq.getQuantity());
        return this.cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long id) throws IdInvalidException {
        CartItem cartItem = this.getCartItemById(id);
        this.cartItemRepository.deleteById(cartItem.getId());
    }
}
