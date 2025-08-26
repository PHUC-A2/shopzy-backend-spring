package com.example.shopzy.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.shopzy.domain.Cart;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.repository.CartRepository;
import com.example.shopzy.util.error.IdInvalidException;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart createCart(Cart cart) {
        return this.cartRepository.save(cart);
    }

    public ResultPaginationDTO getAllCarts(Specification<Cart> spec, Pageable pageable) {
        Page<Cart> pageCart = this.cartRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageCart.getTotalPages());
        mt.setTotal(pageCart.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCart.getContent());

        return rs;

    }

    public Cart getCartById(Long id) throws IdInvalidException {
        Optional<Cart> cartOptional = this.cartRepository.findById(id);
        if (cartOptional.isPresent()) {
            return cartOptional.get();
        } else {
            throw new IdInvalidException("Không tìm thấy Cart với ID = " + id);
        }

    }

    public Cart updateCart(Cart cartReq) throws IdInvalidException {
        Cart cart = this.getCartById(cartReq.getId());
        cart.setUserId(cartReq.getUserId());
        return this.cartRepository.save(cart);
    }

    public void deleteCart(Long id) throws IdInvalidException {
        Cart cart = this.getCartById(id);
        this.cartRepository.deleteById(cart.getId());
    }
}
