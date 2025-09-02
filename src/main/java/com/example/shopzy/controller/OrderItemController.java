package com.example.shopzy.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shopzy.domain.entity.OrderItem;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.domain.response.orderitem.ResOrderItemDTO;
import com.example.shopzy.service.OrderItemService;
import com.example.shopzy.util.annotation.ApiMessage;
import com.example.shopzy.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1/")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // Create
    @PostMapping("/order-items")
    @ApiMessage("Create an order item")
    public ResponseEntity<ResOrderItemDTO> createOrderItem(@RequestBody OrderItem orderItemReq)
            throws IdInvalidException {
        OrderItem created = this.orderItemService.createOrderItem(orderItemReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderItemService.convertToResOrderItemDTO(created));
    }

    // Get all
    @GetMapping("/order-items")
    @ApiMessage("Get all order items")
    public ResponseEntity<ResultPaginationDTO> getAllOrderItems(@Filter Specification<OrderItem> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.orderItemService.getAllOrderItems(spec, pageable));
    }

    // Get by ID
    @GetMapping("/order-items/{id}")
    @ApiMessage("Get order item by id")
    public ResponseEntity<ResOrderItemDTO> getOrderItemById(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity
                .ok(this.orderItemService.convertToResOrderItemDTO(this.orderItemService.getOrderItemById(id)));
    }

    // Update
    @PutMapping("/order-items")
    @ApiMessage("Update an order item")
    public ResponseEntity<ResOrderItemDTO> updateOrderItem(@RequestBody OrderItem orderItemReq)
            throws IdInvalidException {
        OrderItem updated = this.orderItemService.updateOrderItem(orderItemReq);
        return ResponseEntity.ok(this.orderItemService.convertToResOrderItemDTO(updated));
    }

    // Delete
    @DeleteMapping("/order-items/{id}")
    @ApiMessage("Delete an order item")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable("id") Long id) throws IdInvalidException {
        this.orderItemService.deleteOrderItem(id);
        return ResponseEntity.ok(null);
    }
}
