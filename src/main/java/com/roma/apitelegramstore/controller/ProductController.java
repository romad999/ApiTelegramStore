package com.roma.apitelegramstore.controller;

import com.roma.apitelegramstore.dto.ProductRequestDto;
import com.roma.apitelegramstore.model.Product;
import com.roma.apitelegramstore.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // вывести все продкуты, использует сервис
    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    // вывести все продкуты, использует сервис
    @PostMapping
    public Product create(@RequestBody ProductRequestDto dto) {

        Product product = new Product();
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        return productService.createProduct(product);
    }

    // купить продукт, использует сервис
    @PutMapping("/{id}/buy")
    public Product buy(@PathVariable Long id, @RequestParam Integer quantity) {
        return productService.buyProduct(id, quantity);
    }
}
