package com.roma.apitelegramstore.repository;

import com.roma.apitelegramstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
// а репозиторий пустой. Он просто наследуется от JpaRepository
}
