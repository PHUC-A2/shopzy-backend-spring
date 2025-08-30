package com.example.shopzy.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.shopzy.domain.Order;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.repository.OrderRepository;
import com.example.shopzy.util.error.IdInvalidException;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Tạo mới Order
    public Order createOrder(Order order) {
        return this.orderRepository.save(order);
    }

    // Lấy tất cả Orders (có filter + phân trang)
    public ResultPaginationDTO getAllOrders(Specification<Order> spec, Pageable pageable) {
        Page<Order> pageOrder = this.orderRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageOrder.getTotalPages());
        mt.setTotal(pageOrder.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageOrder.getContent());

        return rs;
    }

    // Lấy Order theo ID
    public Order getOrderById(Long id) throws IdInvalidException {
        Optional<Order> orderOptional = this.orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new IdInvalidException("Không tìm thấy Order với ID = " + id);
        }
    }

    // Cập nhật Order
    public Order updateOrder(Order orderReq) throws IdInvalidException {
        Order order = this.getOrderById(orderReq.getId());

        // cập nhật các field
        order.setUserId(orderReq.getUserId());
        order.setStatus(orderReq.getStatus());
        order.setPaymentMethod(orderReq.getPaymentMethod());
        order.setPaymentStatus(orderReq.getPaymentStatus());
        order.setTotal(orderReq.getTotal());
        order.setShippingAddress(orderReq.getShippingAddress());
        order.setShippingPhone(orderReq.getShippingPhone());

        return this.orderRepository.save(order);
    }

    // Xoá Order
    public void deleteOrder(Long id) throws IdInvalidException {
        Order order = this.getOrderById(id);
        this.orderRepository.deleteById(order.getId());
    }
}
