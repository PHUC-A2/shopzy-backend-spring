package com.example.shopzy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.shopzy.domain.entity.Order;
import com.example.shopzy.domain.entity.User;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.domain.response.order.ResOrderDTO;
import com.example.shopzy.repository.OrderRepository;
import com.example.shopzy.util.error.IdInvalidException;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    // Tạo mới Order
    public Order createOrder(Order order) {
        // check user
        User user = this.userService.getUserById(order.getUser().getId());
        if (user != null) {
            order.setUser(user);
        } else {
            return null;
        }
        return this.orderRepository.save(order);
    }

    // Lấy tất cả Orders (có filter + phân trang)
    public ResultPaginationDTO getAllOrders(Specification<Order> spec, Pageable pageable) {
        Page<Order> pageOrder = this.orderRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageOrder.getTotalPages());
        mt.setTotal(pageOrder.getTotalElements());

        rs.setMeta(mt);

        List<Order> orders = pageOrder.getContent();
        List<ResOrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(convertToResOrderDTO(order));
        }

        rs.setResult(orderDTOs);
        // rs.setResult(pageOrder.getContent());

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

        // check user
        User user = this.userService.getUserById(orderReq.getUser().getId());
        if (user != null) {
            order.setUser(user);
        } else {
            order.setUser(null);
        }

        // cập nhật các field
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

    public ResOrderDTO convertToResOrderDTO(Order order) {
        ResOrderDTO res = new ResOrderDTO();
        res.setId(order.getId());
        res.setStatus(order.getStatus());
        res.setPaymentMethod(order.getPaymentMethod());
        res.setPaymentStatus(order.getPaymentStatus());
        res.setTotal(order.getTotal());
        res.setShippingAddress(order.getShippingAddress());
        res.setShippingPhone(order.getShippingPhone());
        res.setCreatedAt(order.getCreatedAt());
        res.setCreatedBy(order.getCreatedBy());
        res.setUpdatedAt(order.getUpdatedAt());
        res.setUpdatedBy(order.getUpdatedBy());

        // check user có trong DB không
        if (order.getUser() != null) {
            res.setUser(new ResOrderDTO.OrderUser(
                    order.getUser().getId(),
                    order.getUser().getName(),
                    order.getUser().getFullName(),
                    order.getUser().getEmail(),
                    order.getUser().getPhoneNumber()));
        } else {
            res.setUser(null);
        }

        return res;
    }
}
