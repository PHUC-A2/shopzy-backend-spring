package com.example.shopzy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.shopzy.domain.entity.Cart;
import com.example.shopzy.domain.entity.User;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.domain.response.cart.ResCartDTO;
import com.example.shopzy.repository.CartRepository;
import com.example.shopzy.util.error.IdInvalidException;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;

    public CartService(CartRepository cartRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    public Cart createCart(Cart cart) {
        User user = this.userService.getUserById(cart.getUser().getId());
        if (user != null) {
            cart.setUser(user);
            return this.cartRepository.save(cart);
        }
        return null;
    }

    public ResultPaginationDTO getAllCarts(Specification<Cart> spec, Pageable pageable) {
        Page<Cart> pageCart = this.cartRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageCart.getTotalPages());
        mt.setTotal(pageCart.getTotalElements());

        rs.setMeta(mt);

        // convert

        List<Cart> carts = pageCart.getContent();
        List<ResCartDTO> resCartDTOs = new ArrayList<>();
        for (Cart cart : carts) {
            resCartDTOs.add(convertToResCartDTO(cart));
        }
        rs.setResult(resCartDTOs);
        // rs.setResult(pageCart.getContent());

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
        User user = this.userService.getUserById(cartReq.getUser().getId());
        if (user != null) {
            cart.setUser(user);
        } else {
            return null;
        }
        return this.cartRepository.save(cart);
    }

    public void deleteCart(Long id) throws IdInvalidException {
        Cart cart = this.getCartById(id);
        this.cartRepository.deleteById(cart.getId());
    }

    public ResCartDTO convertToResCartDTO(Cart cart) {
        ResCartDTO res = new ResCartDTO();
        res.setId(cart.getId());
        res.setCreatedAt(cart.getCreatedAt());
        res.setCreatedBy(cart.getCreatedBy());
        res.setUpdatedAt(cart.getUpdatedAt());
        res.setUpdatedBy(cart.getUpdatedBy());

        // check user có trong DB ?
        if (cart.getUser() != null) {
            res.setUser(new ResCartDTO.CartUser(
                    cart.getUser().getId(),
                    cart.getUser().getName(),
                    cart.getUser().getFullName(),
                    cart.getUser().getEmail(),
                    cart.getUser().getPhoneNumber()));
        } else {
            res.setUser(null);
        }
        return res;
    }
}
