package com.judy.ecommerce.backend.dto.cart;

public record CartProductDTO(
        long id,
        String name,
        double price,
        double salePrice,
        double salePercent,
        int quantity
) {}
