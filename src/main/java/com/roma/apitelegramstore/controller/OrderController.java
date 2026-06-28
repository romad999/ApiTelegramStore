package com.roma.apitelegramstore.controller;

import com.roma.apitelegramstore.dto.OrderRequestDto;
import com.roma.apitelegramstore.model.Order;
import com.roma.apitelegramstore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order create(@Valid @RequestBody OrderRequestDto dto) {
        return orderService.createOrder(
                dto.getProductId(),
                dto.getQuantity(),
                dto.getCustomerName()
        );
    }
}
