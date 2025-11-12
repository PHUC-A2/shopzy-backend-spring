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
import com.example.shopzy.domain.response.cart.ResCartClientDTO;
import com.example.shopzy.domain.response.cart.ResCartDTO;
import com.example.shopzy.domain.response.cartitem.ResCartItemClientDTO;
import com.example.shopzy.repository.CartRepository;
import com.example.shopzy.util.SecurityUtil;
import com.example.shopzy.util.constant.cart.CartStatusEnum;
import com.example.shopzy.util.error.IdInvalidException;

import jakarta.transaction.Transactional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, UserService userService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.productService = productService;
    }

    public Cart createCart(Cart cart) throws IdInvalidException {
        User user = this.userService.getUserById(cart.getUser().getId());
        cart.setUser(user);
        return this.cartRepository.save(cart);
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
        cart.setStatus(cartReq.getStatus());
        cart.setUser(user);
        return this.cartRepository.save(cart);
    }

    public void deleteCart(Long id) throws IdInvalidException {
        Cart cart = this.getCartById(id);
        this.cartRepository.deleteById(cart.getId());
    }

    public ResCartDTO convertToResCartDTO(Cart cart) {
        ResCartDTO res = new ResCartDTO();
        res.setId(cart.getId());
        res.setStatus(cart.getStatus());
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

    // cart client
    public ResCartClientDTO convertToResCartClientDTO(Long cartId) throws IdInvalidException {

        // lấy giỏ hàng từ DB để chắc chắn dữ liệu mới nhất
        Cart cartDB = this.getCartById(cartId);

        // Lấy user hiện tại từ SecurityContext
        String currentUserEmail = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new IdInvalidException("User not logged in"));

        // Kiểm tra user trong cart có khớp với user hiện tại không
        if (!cartDB.getUser().getEmail().equals(currentUserEmail)) {
            throw new IdInvalidException("Bạn không có quyền xem giỏ hàng này");
        }

        ResCartClientDTO res = new ResCartClientDTO();
        res.setCartId(cartDB.getId());

        List<ResCartItemClientDTO> cartItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (CartItem ci : cartDB.getCartItems()) {
            Product p = ci.getProduct();

            if (p == null) {
                throw new IdInvalidException("Product not found for cart item with ID = " + ci.getId());
            }

            double subtotal = ci.getQuantity() * p.getPrice();

            ResCartItemClientDTO itemDTO = new ResCartItemClientDTO(
                    ci.getId(), // cartItemId
                    p.getId(), // productId
                    p.getName(), // name
                    p.getImageUrl(), // imageUrl
                    p.getPrice(), // price
                    ci.getQuantity(), // quantity
                    p.getSize(), // size
                    p.getColor(), // color
                    p.getStatus(), // status
                    subtotal // subtotal
            );

            cartItems.add(itemDTO);
            totalPrice += subtotal;
        }

        res.setCartItems(cartItems);
        res.setTotalPrice(totalPrice);

        return res;
    }

    public Cart getOrCreateCartForCurrentUser() throws IdInvalidException {
        String currentUserEmail = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new IdInvalidException("User not logged in"));

        User user = userService.handleGetUserByUsername(currentUserEmail);

        // Lấy cart OPEN duy nhất của user
        Cart cart = cartRepository.findByUserIdAndStatus(user.getId(), CartStatusEnum.OPEN)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setStatus(CartStatusEnum.OPEN);
                    return cartRepository.save(newCart);
                });

        return cart;
    }

    // public ResCartClientDTO addProductToCart(Long productId, int quantity) throws
    // IdInvalidException {
    // Cart cart = getOrCreateCartForCurrentUser();

    // Optional<CartItem> existingItem = cart.getCartItems().stream()
    // .filter(ci -> ci.getProduct().getId().equals(productId))
    // .findFirst();

    // if (existingItem.isPresent()) {
    // CartItem ci = existingItem.get();
    // ci.setQuantity(ci.getQuantity() + quantity);
    // } else {
    // Product product = productService.getProductById(productId);
    // CartItem newItem = new CartItem();
    // newItem.setCart(cart);
    // newItem.setProduct(product);
    // newItem.setQuantity(quantity);
    // cart.getCartItems().add(newItem);
    // }

    // cartRepository.save(cart);
    // return convertToResCartClientDTO(cart.getId());
    // }

    @Transactional
    public ResCartClientDTO addProductToCart(Long productId, int quantity) throws IdInvalidException {
        Cart cart = getOrCreateCartForCurrentUser();

        // Bước 1: Lấy product từ DB, nếu không có thì báo lỗi
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new IdInvalidException("Không tìm thấy sản phẩm với ID = " + productId);
        }

        // Bước 2: Kiểm tra xem product đã có trong giỏ hàng chưa
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(ci -> ci.getProduct() != null && ci.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Nếu có -> cộng thêm số lượng
            CartItem ci = existingItem.get();
            ci.setQuantity(ci.getQuantity() + quantity);
        } else {
            // Nếu chưa có -> tạo mới
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);

            // Đảm bảo mối quan hệ 2 chiều được thiết lập
            cart.getCartItems().add(newItem);
        }

        // Lưu giỏ hàng (có cascade xuống CartItem)
        Cart savedCart = cartRepository.save(cart);

        // Trả kết quả cập nhật
        return convertToResCartClientDTO(savedCart.getId());
    }

    public ResCartClientDTO checkoutCart() throws IdInvalidException {
        Cart cart = getOrCreateCartForCurrentUser();
        if (cart.getCartItems().isEmpty()) {
            throw new IdInvalidException("Giỏ hàng trống, không thể thanh toán");
        }
        cart.setStatus(CartStatusEnum.CHECKED_OUT);
        cartRepository.save(cart);
        return convertToResCartClientDTO(cart.getId());
    }

    // xóa cartItemId
    @Transactional
    public ResCartClientDTO removeProductFromCart(Long cartItemId) throws IdInvalidException {
        // Lấy giỏ hàng của user hiện tại
        Cart cart = getOrCreateCartForCurrentUser();

        // Tìm cartItem cần xóa trong giỏ
        Optional<CartItem> itemToRemove = cart.getCartItems().stream()
                .filter(ci -> ci.getId().equals(cartItemId))
                .findFirst();

        if (itemToRemove.isEmpty()) {
            throw new IdInvalidException("Không tìm thấy sản phẩm trong giỏ hàng với ID = " + cartItemId);
        }

        // Xóa item khỏi giỏ
        cart.getCartItems().remove(itemToRemove.get());

        // Lưu lại cart (cascade sẽ tự động cập nhật CartItem)
        cartRepository.save(cart);

        System.out.println("User cart ID: " + cart.getId());
        System.out.println("Cart items count: " + cart.getCartItems().size());
        System.out.println("Trying to remove cartItemId: " + cartItemId);

        // Trả về giỏ hàng mới nhất
        return convertToResCartClientDTO(cart.getId());
    }

    @Transactional
    public ResCartClientDTO updateQuantityByProduct(Long productId, int delta) throws IdInvalidException {
        Cart cart = getOrCreateCartForCurrentUser();

        Optional<CartItem> optionalItem = cart.getCartItems().stream()
                .filter(ci -> ci.getProduct() != null && ci.getProduct().getId().equals(productId))
                .findFirst();

        if (optionalItem.isEmpty()) {
            throw new IdInvalidException("Sản phẩm không tồn tại trong giỏ hàng");
        }

        CartItem cartItem = optionalItem.get();
        int newQuantity = cartItem.getQuantity() + delta;
        if (newQuantity <= 0) {
            throw new IdInvalidException("Số lượng phải lớn hơn 0");
        }

        cartItem.setQuantity(newQuantity);
        cartRepository.save(cart); // cascade sẽ lưu cartItem

        return convertToResCartClientDTO(cart.getId());
    }

}
