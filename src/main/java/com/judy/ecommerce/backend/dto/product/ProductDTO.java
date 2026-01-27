package com.judy.ecommerce.backend.dto.product;

import java.util.Optional;

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
