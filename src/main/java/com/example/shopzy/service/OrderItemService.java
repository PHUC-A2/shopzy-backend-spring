package com.example.shopzy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.shopzy.domain.entity.Order;
import com.example.shopzy.domain.entity.OrderItem;
import com.example.shopzy.domain.entity.Product;
import com.example.shopzy.domain.entity.User;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.domain.response.orderitem.ResOrderItemDTO;
import com.example.shopzy.repository.OrderItemRepository;
import com.example.shopzy.util.error.IdInvalidException;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final OrderService orderService;

    public OrderItemService(OrderItemRepository orderItemRepository, ProductService productService,
            OrderService orderService) {
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
        this.orderService = orderService;
    }

    // Create
    public OrderItem createOrderItem(OrderItem orderItem) throws IdInvalidException {

        Long orderId = orderItem.getOrder().getId();
        Long productId = orderItem.getProduct().getId();

        boolean isValid = this.orderItemRepository.existsByOrderIdAndProductId(orderId, productId);

        if (isValid) {
            throw new IdInvalidException(
                    "Product với ID = " + productId + " và Order với ID = " + orderId + " đã tồn tại");
        }

        // check product
        Product product = this.productService.getProductById(orderItem.getProduct().getId());
        orderItem.setProduct(product);

        // check order + user
        Order order = this.orderService.getOrderById(orderItem.getOrder().getId());
        orderItem.setOrder(order);

        return this.orderItemRepository.save(orderItem);
    }

    // Get all with pagination + filter
    public ResultPaginationDTO getAllOrderItems(Specification<OrderItem> spec, Pageable pageable) {
        Page<OrderItem> pageOrderItem = this.orderItemRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageOrderItem.getTotalPages());
        mt.setTotal(pageOrderItem.getTotalElements());

        rs.setMeta(mt);

        List<OrderItem> orderItems = pageOrderItem.getContent();
        List<ResOrderItemDTO> orderItemDTOs = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            orderItemDTOs.add(convertToResOrderItemDTO(orderItem));
        }

        rs.setResult(orderItemDTOs);
        // rs.setResult(pageOrderItem.getContent());

        return rs;
    }

    // Get by ID
    public OrderItem getOrderItemById(Long id) throws IdInvalidException {
        Optional<OrderItem> orderItemOptional = this.orderItemRepository.findById(id);
        if (orderItemOptional.isPresent()) {
            return orderItemOptional.get();
        } else {
            throw new IdInvalidException("Không tìm thấy OrderItem với ID = " + id);
        }
    }

    // Update
    public OrderItem updateOrderItem(OrderItem orderItemReq) throws IdInvalidException {
        OrderItem orderItem = this.getOrderItemById(orderItemReq.getId());

        Long orderId = orderItemReq.getOrder().getId();
        Long productId = orderItemReq.getProduct().getId();

        boolean exists = this.orderItemRepository.existsByOrderIdAndProductIdAndIdNot(orderId, productId,
                orderItem.getId());
        if (exists) {
            throw new IdInvalidException(
                    "Product với ID = " + productId + " và Order với ID = " + orderId + " đã tồn tại trong order khác");
        }
        orderItem.setQuantity(orderItemReq.getQuantity());
        orderItem.setUnitPrice(orderItemReq.getUnitPrice());

        // check product
        Product product = this.productService.getProductById(productId);
        orderItem.setProduct(product);

        // check order
        Order order = this.orderService.getOrderById(orderId);
        orderItem.setOrder(order);

        return this.orderItemRepository.save(orderItem);
    }

    // Delete
    public void deleteOrderItem(Long id) throws IdInvalidException {
        OrderItem orderItem = this.getOrderItemById(id);
        this.orderItemRepository.deleteById(orderItem.getId());
    }

    public ResOrderItemDTO convertToResOrderItemDTO(OrderItem orderItem) {
        ResOrderItemDTO res = new ResOrderItemDTO();
        res.setId(orderItem.getId());
        res.setQuantity(orderItem.getQuantity());
        res.setUnitPrice(orderItem.getUnitPrice());
        res.setCreatedAt(orderItem.getCreatedAt());
        res.setCreatedBy(orderItem.getCreatedBy());
        res.setUpdatedAt(orderItem.getUpdatedAt());
        res.setUpdatedBy(orderItem.getUpdatedBy());

        // check và set product
        if (orderItem.getProduct() != null) {
            res.setProduct(new ResOrderItemDTO.OrderItemProduct(
                    orderItem.getProduct().getId(),
                    orderItem.getProduct().getName(),
                    orderItem.getProduct().getDescription(),
                    orderItem.getProduct().getPrice(),
                    orderItem.getProduct().getStock(),
                    orderItem.getProduct().getStatus(),
                    orderItem.getProduct().getProductCondition(),
                    orderItem.getProduct().getImageUrl(),
                    orderItem.getProduct().getSize(),
                    orderItem.getProduct().getColor()));
        }

        // check order
        if (orderItem.getOrder() != null) {
            User user = orderItem.getOrder().getUser();
            ResOrderItemDTO.OrderItemUser orderItemUser = null;

            if (user != null) {
                orderItemUser = new ResOrderItemDTO.OrderItemUser(
                        user.getId(),
                        user.getName(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getPhoneNumber());
            }

            res.setOrder(new ResOrderItemDTO.OrderItemOrder(
                    orderItem.getOrder().getId(),
                    orderItem.getOrder().getStatus(),
                    orderItem.getOrder().getPaymentMethod(),
                    orderItem.getOrder().getPaymentStatus(),
                    orderItem.getOrder().getTotal(),
                    orderItem.getOrder().getShippingAddress(),
                    orderItem.getOrder().getShippingPhone(),
                    orderItem.getOrder().getCreatedAt(),
                    orderItem.getOrder().getUpdatedAt(),
                    orderItem.getOrder().getCreatedBy(),
                    orderItem.getOrder().getUpdatedBy(),
                    orderItemUser));
        }

        return res;
    }

}
