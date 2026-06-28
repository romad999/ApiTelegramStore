package com.roma.apitelegramstore.service;

import com.roma.apitelegramstore.exception.NotEnoughStockException;
import com.roma.apitelegramstore.exception.ProductNotFoundException;
import com.roma.apitelegramstore.model.Order;
import com.roma.apitelegramstore.model.Product;
import com.roma.apitelegramstore.repository.OrderRepository;
import com.roma.apitelegramstore.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(Long productId, Integer quantity, String customerName) {
        // 1. Ищем товар в базе.
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + productId + " не найден!"));

        // 2. Проверяем, хватает ли товара на складе
        if (product.getQuantity() < quantity) {
            throw new NotEnoughStockException("Недостаточно товара! Доступно: " + product.getQuantity());
        }

        // 3. Уменьшаем количество товара на складе и сохраняем его
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        // 4. Создаем новый объект заказа и заполняем его данными
        Order order = new Order();
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setCustomerName(customerName);

        // 5. Сохраняем заказ в базу данных
        return orderRepository.save(order);
    }
}
