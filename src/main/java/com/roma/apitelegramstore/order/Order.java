package com.roma.apitelegramstore.order;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.roma.apitelegramstore.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY, чтобы не тащить юзера из базы каждый раз, когда нам нужен просто заказ
    @JoinColumn(name = "user_id", nullable = false) // Указываем имя колонки в БД
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // МАГИЯ JPA: Один заказ содержит МНОГО элементов корзины!
    // mappedBy = "order" говорит, что связь настроена в классе OrderItem в поле order
    // cascade = CascadeType.ALL означает: если мы сохраняем заказ, все товары в его корзине сохранятся автоматически!
    // Говорим: "Это главная связь, её нужно превращать в JSON"
    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}
