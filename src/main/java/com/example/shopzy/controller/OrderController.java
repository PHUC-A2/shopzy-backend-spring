package com.example.shopzy.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shopzy.domain.Order;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.service.OrderService;
import com.example.shopzy.util.annotation.ApiMessage;
import com.example.shopzy.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1/")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create Order
    @PostMapping("/orders")
    @ApiMessage("Create an order")
    public ResponseEntity<Order> createOrder(@RequestBody Order orderReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderService.createOrder(orderReq));
    }

    // Get all Orders (with filter + pagination)
    @GetMapping("/orders")
    @ApiMessage("Get all orders")
    public ResponseEntity<ResultPaginationDTO> getAllOrders(@Filter Specification<Order> spec, Pageable pageable) {
        return ResponseEntity.ok(this.orderService.getAllOrders(spec, pageable));
    }

    // Get Order by ID
    @GetMapping("/orders/{id}")
    @ApiMessage("Get order by id")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(this.orderService.getOrderById(id));
    }

    // Update Order
    @PutMapping("/orders")
    @ApiMessage("Update an order")
    public ResponseEntity<Order> updateOrder(@RequestBody Order orderReq) throws IdInvalidException {
        Order order = this.orderService.updateOrder(orderReq);
        return ResponseEntity.ok(order);
    }

    // Delete Order
    @DeleteMapping("/orders/{id}")
    @ApiMessage("Delete an order")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) throws IdInvalidException {
        this.orderService.deleteOrder(id);
        return ResponseEntity.ok(null);
    }
}
