package com.roma.apitelegramstore.controller;

import com.roma.apitelegramstore.dto.ProductRequestDto;
import com.roma.apitelegramstore.mapper.ProductMapper;
import com.roma.apitelegramstore.model.Product;
import com.roma.apitelegramstore.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    // вывести все продкуты, использует сервис
    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    // вывести все продкуты, использует сервис и маппер
    @PostMapping
    public Product create(@Valid @RequestBody ProductRequestDto dto) {
        Product product = productMapper.toEntity(dto);
        return productService.createProduct(product);
    }

    // купить продукт, использует сервис
    @PutMapping("/{id}/buy")
    public Product buy(@PathVariable Long id, @RequestParam Integer quantity) {
        return productService.buyProduct(id, quantity);
    }
}
