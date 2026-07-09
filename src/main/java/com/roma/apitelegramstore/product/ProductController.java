package com.roma.apitelegramstore.product;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public Page<Product> getAll(
            @RequestParam(defaultValue = "0") int page,         // Если клиент не указал страницу, берем 0-ю
            @RequestParam(defaultValue = "10") int size,        // Если не указал размер, выдаем по 10 штук
            @RequestParam(defaultValue = "id") String sortBy,    // Сортировка по умолчанию по ID
            @RequestParam(defaultValue = "asc") String direction // По умолчанию по возрастанию
    ) {
        return productService.getAllProducts(page, size, sortBy, direction);
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
