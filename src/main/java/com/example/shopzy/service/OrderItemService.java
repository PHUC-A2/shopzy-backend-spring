package com.example.shopzy.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.shopzy.domain.OrderItem;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.repository.OrderItemRepository;
import com.example.shopzy.util.error.IdInvalidException;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    // Create
    public OrderItem createOrderItem(OrderItem orderItem) {
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
        rs.setResult(pageOrderItem.getContent());

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
        orderItem.setProductId(orderItemReq.getProductId());
        orderItem.setQuantity(orderItemReq.getQuantity());
        orderItem.setUnitPrice(orderItemReq.getUnitPrice());

        return this.orderItemRepository.save(orderItem);
    }

    // Delete
    public void deleteOrderItem(Long id) throws IdInvalidException {
        OrderItem orderItem = this.getOrderItemById(id);
        this.orderItemRepository.deleteById(orderItem.getId());
    }
}
