package com.roma.apitelegramstore.repository;

import com.roma.apitelegramstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
