package com.back.domain.product.product.dto;

import java.util.List;

public record CreateProductRequest(
        String name,
        String category,
        String description,
        int price,
        List<String> suggestions
) {
}
