package com.roma.apitelegramstore.order;

import com.roma.apitelegramstore.exception.NotEnoughStockException;
import com.roma.apitelegramstore.exception.ProductNotFoundException;
import com.roma.apitelegramstore.product.Product;
import com.roma.apitelegramstore.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(String customerName, List<OrderItemDto> itemDtos) {

        // 1. Создаем сам объект Заказа (пока пустой, только с именем)
        Order order = new Order();
        order.setCustomerName(customerName);

        // 2. Запускаем конвейер Stream API, чтобы превратить DTO в элементы заказа
        List<OrderItem> orderItems = itemDtos.stream()
                .map(dto -> {
                    // Шаг А: Ищем каждый товар на складе по его ID
                    Product product = productRepository.findById(dto.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + dto.getProductId() + " не найден!"));

                    // Шаг Б: Проверяем, хватает ли именно этого товара
                    if (product.getQuantity() < dto.getQuantity()) {
                        throw new NotEnoughStockException("Недостаточно товара " + product.getTitle() + "! Доступно: " + product.getQuantity());
                    }

                    // Шаг В: Списываем количество со склада
                    product.setQuantity(product.getQuantity() - dto.getQuantity());
                    productRepository.save(product); // Сохраняем обновленный склад

                    // Шаг Г: Собираем Java-объект элемента корзины
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order); // Привязываем к нашему общему заказу
                    orderItem.setProduct(product);
                    orderItem.setQuantity(dto.getQuantity());

                    return orderItem; // Отправляем готовый элемент дальше по конвейеру
                })
                .toList(); // Собираем все элементы в один итоговый список

        // 3. Закидываем собранную корзину в наш заказ
        order.setItems(orderItems);

        // 4. Сохраняем заказ в базу данных!
        // Благодаря настройке cascade = CascadeType.ALL в классе Order,
        // Spring сам автоматически сохранит и все элементы order_items в DataGrip!
        return orderRepository.save(order);
    }

}
