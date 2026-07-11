package com.roma.apitelegramstore.order;

import com.roma.apitelegramstore.exception.NotEnoughStockException;
import com.roma.apitelegramstore.exception.ProductNotFoundException;
import com.roma.apitelegramstore.product.Product;
import com.roma.apitelegramstore.product.ProductRepository;
import com.roma.apitelegramstore.user.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final TelegramNotificationService telegramNotificationService; // НАШ НОВЫЙ ПОЧТАЛЬОН


    OrderService(OrderRepository orderRepository,
                 ProductRepository productRepository,
                 TelegramNotificationService telegramNotificationService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.telegramNotificationService = telegramNotificationService;
    }


    public List<Order> getCurrentUserOrderHistory() {
        // Снова достаем текущего юзера из контекста
        User currentUser = (User) Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getPrincipal();

        // Возвращаем только его заказы
        assert currentUser != null;
        return orderRepository.findByUserId(currentUser.getId());
    }


    @Transactional
    public Order createOrder(List<OrderItemDto> itemDtos) { // Убрали String customerName из аргументов

        // 1. Достаем текущего авторизованного пользователя из Spring Security
        User currentUser = (User) Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getPrincipal();

        // 2. Создаем объект Заказа и привязываем к нему нашего юзера
        Order order = new Order();
        order.setUser(currentUser); // Вместо setCustomerName

        // 3. Запускаем конвейер Stream API (тут всё остаётся ОДИН В ОДИН, как у тебя)
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
                    productRepository.save(product);

                    // Шаг Г: Собираем Java-объект элемента корзины
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(dto.getQuantity());

                    return orderItem;
                })
                .toList();

        // 4. Закидываем собранную корзину в наш заказ
        order.setItems(orderItems);

        // 5. Сохраняем заказ в базу данных
        Order savedOrder = orderRepository.save(order);

        // 6. Отправляем реальное уведомление в Telegram
        telegramNotificationService.sendOrderNotification(savedOrder);

        return savedOrder;
    }
}

