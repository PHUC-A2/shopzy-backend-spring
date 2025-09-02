package com.example.shopzy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.shopzy.domain.entity.OrderItem;
import com.example.shopzy.domain.entity.Product;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.domain.response.orderitem.ResOrderItemDTO;
import com.example.shopzy.repository.OrderItemRepository;
import com.example.shopzy.util.error.IdInvalidException;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    // Create
    public OrderItemService(OrderItemRepository orderItemRepository, ProductService productService) {
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
    }

    public OrderItem createOrderItem(OrderItem orderItem) throws IdInvalidException {

        // check product
        Product product = this.productService.getProductById(orderItem.getProduct().getId());
        orderItem.setProduct(product);

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

        orderItem.setOrderId(orderItemReq.getOrderId());
        orderItem.setQuantity(orderItemReq.getQuantity());
        orderItem.setUnitPrice(orderItemReq.getUnitPrice());

        // check product 
        Product product = this.productService.getProductById(orderItemReq.getProduct().getId());
        orderItem.setProduct(product);
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
        res.setOrderId(orderItem.getOrderId()); // tương tự xử lý như với Product
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
        return res;
    }
}
