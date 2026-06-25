package com.roma.apitelegramstore.controller;

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

    // вывести все продкуты, но используя сервис, просто просим его сделать это
    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    // вывести все продкуты, но используя сервис, просто просим его сделать это
    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    // купить продукт, использует сервис
    @PutMapping("/{id}/buy") // мы тут и ссылку для айди, и параметр для количества сделали, типо для удобства?
    public Product buy(@PathVariable Long id, @RequestParam Integer quantity) {
        return productService.buyProduct(id, quantity);
    }
}
