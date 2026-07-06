package com.roma.apitelegramstore.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.roma.apitelegramstore.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с заказом: много элементов относятся к одному заказу
    // Говорим: "А это обратный путь, его в JSON превращать НЕ НАДО (игнорируй), чтобы не было петли!"
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Связь с товаром: много элементов могут ссылаться на один и тот же товар
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity; // Количество конкретно этого товара в корзине
}
