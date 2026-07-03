package com.roma.apitelegramstore.product;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    // 1. вывести все продкуты, использует сервис
    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    // 2. вывести все продкуты, использует сервис и маппер
    @PostMapping
    public Product create(@Valid @RequestBody ProductRequestDto dto) {
        Product product = productMapper.toEntity(dto);
        return productService.createProduct(product);
    }

    // 3. GET запрос на /api/products/{id}
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // 4. PUT запрос на /api/products/{id}
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody ProductRequestDto dto) {
        return productService.updateProduct(id, dto);
    }

    // 5. DELETE запрос на /api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
