package com.roma.apitelegramstore.service;

import com.roma.apitelegramstore.dto.ProductRequestDto;
import com.roma.apitelegramstore.exception.NotEnoughStockException;
import com.roma.apitelegramstore.exception.ProductNotFoundException;
import com.roma.apitelegramstore.model.Product;
import com.roma.apitelegramstore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 1. вывести все продкуты, но используя репозиторий
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 2. создать продукт, но используя .save из репозитория. сам репозиторий пуст но наследуется от JpaRepository
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // 3. купить продукт, использует репозиторий, и два класса ошибки
    public Product buyProduct(Long id, Integer quantityToBuy) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + id + " не найден!"));

        if (product.getQuantity() < quantityToBuy) {
            throw new NotEnoughStockException("Недостаточно товара на складе! Доступно: " + product.getQuantity());
        }

        product.setQuantity(product.getQuantity() - quantityToBuy);

        return productRepository.save(product);
    }

    // 4. найти продукт по id
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + id + " не найден!"));
    }

    // 5. Обновить товар
    public Product updateProduct(Long id, ProductRequestDto dto) {
        Product product = getProductById(id);

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            product.setTitle(dto.getTitle());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getQuantity() != null) {
            product.setQuantity(dto.getQuantity());
        }

        return productRepository.save(product);
    }

    // 6. Удалить товар
    public void deleteProduct(Long id) {
        Product product = getProductById(id);

        productRepository.delete(product);
    }
}
