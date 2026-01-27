package com.judy.ecommerce.backend.dto.product;

public record ProductDTO(
        long id,
        String name,
        String description,
        String category,
        double price,
        double salePrice,
        double salePercent,
        int quantity,
        boolean disabled
) {}
