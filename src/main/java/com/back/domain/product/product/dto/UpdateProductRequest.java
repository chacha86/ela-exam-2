package com.back.domain.product.product.dto;

public record UpdateProductRequest(
        String name,
        String category,
        String description,
        int price
) {
}
