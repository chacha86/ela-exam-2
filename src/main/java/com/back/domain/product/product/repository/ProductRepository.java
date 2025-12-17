package com.back.domain.product.product.repository;

import com.back.domain.product.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingOrderByCreatedAtDesc(String keyword);
}
