package com.roma.apitelegramstore.product;

import com.roma.apitelegramstore.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }



    // 1. вывести все продкуты
    public Page<Product> getAllProducts(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findAll(pageable);
    }




    // 2. создать продукт, но используя .save из репозитория. сам репозиторий пуст но наследуется от JpaRepository
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // 3. найти продукт по id
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + id + " не найден!"));
    }

    // 4. Обновить товар
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

    // 5. Удалить товар
    public void deleteProduct(Long id) {
        Product product = getProductById(id);

        productRepository.delete(product);
    }
}
