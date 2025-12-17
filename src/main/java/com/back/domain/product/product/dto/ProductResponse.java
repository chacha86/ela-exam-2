package com.back.domain.product.product.dto;

import com.back.domain.product.product.Product;

import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        String category,
        String description,
        int price,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getDescription(),
                product.getPrice(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
