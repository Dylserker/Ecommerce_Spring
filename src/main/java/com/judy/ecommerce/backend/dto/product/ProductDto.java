package com.judy.ecommerce.backend.dto.product;

import com.judy.ecommerce.backend.entity.Categories;

import java.util.List;

public record ProductDto(
        long id,
        String name,
        String description,
        String category,
        double price,
        int quantity
) {}
