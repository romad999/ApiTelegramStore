package com.roma.apitelegramstore.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
// а репозиторий пустой. Он просто наследуется от JpaRepository
}
