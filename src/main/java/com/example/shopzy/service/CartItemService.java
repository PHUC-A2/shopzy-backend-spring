package com.example.shopzy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.shopzy.domain.entity.Cart;
import com.example.shopzy.domain.entity.CartItem;
import com.example.shopzy.domain.entity.Product;
import com.example.shopzy.domain.entity.User;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.domain.response.cartitem.ResCartItemDTO;
import com.example.shopzy.repository.CartItemRepository;
import com.example.shopzy.util.error.IdInvalidException;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartService cartService;

    public CartItemService(CartItemRepository cartItemRepository, ProductService productService,
            CartService cartService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.cartService = cartService;
    }

    public CartItem createCartItem(CartItem cartItem) throws IdInvalidException {

        Long cartId = cartItem.getCart().getId();
        Long productId = cartItem.getProduct().getId();

        boolean exists = this.cartItemRepository.existsByCartIdAndProductId(cartId, productId);
        if (exists) {
            throw new IdInvalidException(
                    "Product với ID = " + productId + " đã tồn tại trong Cart với ID = " + cartId);
        }

        // check product trước nếu có thì truyền vào id không thì trả null
        Product product = this.productService.getProductById(cartItem.getProduct().getId());
        cartItem.setProduct(product);

        // check cart + user(trong cart)
        Cart cart = this.cartService.getCartById(cartItem.getCart().getId());
        cartItem.setCart(cart);

        return this.cartItemRepository.save(cartItem);
    }

    public ResultPaginationDTO getAllCartItems(Specification<CartItem> spec, Pageable pageable) {
        Page<CartItem> pageCartItem = this.cartItemRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageCartItem.getTotalPages());
        mt.setTotal(pageCartItem.getTotalElements());

        rs.setMeta(mt);

        List<CartItem> cartItems = pageCartItem.getContent();
        List<ResCartItemDTO> cartItemDTOs = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            cartItemDTOs.add(convertToResCartItemDTO(cartItem));
        }

        rs.setResult(cartItemDTOs);
        // rs.setResult(pageCartItem.getContent());

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

        Long cartId = cartItemReq.getCart().getId();
        Long productId = cartItemReq.getProduct().getId();

        boolean exists = this.cartItemRepository.existsByCartIdAndProductIdAndIdNot(cartId, productId,
                cartItem.getId());
        if (exists) {
            throw new IdInvalidException(
                    "Product với ID = " + productId + " đã tồn tại trong Cart với ID = " + cartId + " khác");
        }

        // cartItem.setCartId(cartItemReq.getCartId());
        cartItem.setQuantity(cartItemReq.getQuantity());
        // check product nếu có trong DB thì set không thì ném ra ex (đã xử lý ở
        // getProductById)
        Product product = this.productService.getProductById(productId);
        cartItem.setProduct(product);

        // check cart
        Cart cart = this.cartService.getCartById(cartId);
        cartItem.setCart(cart);

        return this.cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long id) throws IdInvalidException {
        CartItem cartItem = this.getCartItemById(id);
        this.cartItemRepository.deleteById(cartItem.getId());
    }

    // convert
    public ResCartItemDTO convertToResCartItemDTO(CartItem cartItem) {
        ResCartItemDTO res = new ResCartItemDTO();

        res.setId(cartItem.getId());
        res.setQuantity(cartItem.getQuantity());
        res.setCreatedAt(cartItem.getCreatedAt());
        res.setCreatedBy(cartItem.getCreatedBy());
        res.setUpdatedAt(cartItem.getUpdatedAt());
        res.setUpdatedBy(cartItem.getUpdatedBy());

        // check và set product
        if (cartItem.getProduct() != null) {
            res.setProduct(new ResCartItemDTO.CartItemProduct(
                    cartItem.getProduct().getId(),
                    cartItem.getProduct().getName(),
                    cartItem.getProduct().getDescription(),
                    cartItem.getProduct().getPrice(),
                    cartItem.getProduct().getStock(),
                    cartItem.getProduct().getStatus(),
                    cartItem.getProduct().getProductCondition(),
                    cartItem.getProduct().getImageUrl(),
                    cartItem.getProduct().getSize(),
                    cartItem.getProduct().getColor()));
        }

        // check cart
        if (cartItem.getCart() != null) {

            // check user để lấy user vì trong cart chứa field obj user
            User user = cartItem.getCart().getUser();
            ResCartItemDTO.CartItemUser cartItemUser = null;
            if (user != null) {
                cartItemUser = new ResCartItemDTO.CartItemUser(
                        user.getId(),
                        user.getName(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getPhoneNumber());
            }

            res.setCart(new ResCartItemDTO.CartItemCart(
                    cartItem.getCart().getId(),
                    cartItemUser));
        }

        return res;
    }
}
