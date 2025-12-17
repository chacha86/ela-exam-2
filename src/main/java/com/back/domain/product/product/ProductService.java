package com.back.domain.product.product;

import com.back.domain.product.product.dto.ProductResponse;
import com.back.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse create(String name, String category, String description, int price, List<String> suggestions) {

        Product product = Product.builder()
                .name(name)
                .category(category)
                .description(description)
                .price(price)
                .suggestions(suggestions)
                .build();

        Product saved = productRepository.save(product);
        return ProductResponse.from(saved);
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse update(Long id, String name, String category, String description, int price) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        product.update(
                name,
                category,
                description,
                price
        );

        return ProductResponse.from(product);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }

    public int count() {
        return (int) productRepository.count();
    }
}
