package com.roma.apitelegramstore.service;

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

    // вывести все продкуты, но используя репозиторий, просто просим его сделать это
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // создать продукт, но используя репозиторий, просто просим его сделать это
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // купить продукт, использует репозиторий, и два класса ошибки
    public Product buyProduct(Long id, Integer quantityToBuy) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + id + " не найден!"));

        if (product.getQuantity() < quantityToBuy) {
            throw new NotEnoughStockException("Недостаточно товара на складе! Доступно: " + product.getQuantity());
        }

        product.setQuantity(product.getQuantity() - quantityToBuy);

        return productRepository.save(product);
    }
}
