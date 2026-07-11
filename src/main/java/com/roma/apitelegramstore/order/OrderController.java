package com.roma.apitelegramstore.order;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order create(@Valid @RequestBody OrderRequestDto dto) {

        // Передаем в сервис имя покупателя и весь прилетевший список товаров из DTO!
        return orderService.createOrder(
                dto.getItems()
        );
    }


    @GetMapping("/history")
    public ResponseEntity<List<Order>> getMyOrderHistory() {
        return ResponseEntity.ok(orderService.getCurrentUserOrderHistory());
    }
}
