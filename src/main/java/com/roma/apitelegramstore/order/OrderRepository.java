package com.roma.apitelegramstore.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Spring Data сам поймет магию имени метода и сделает правильный SQL-запрос!
    List<Order> findByUserId(Long userId);
}
