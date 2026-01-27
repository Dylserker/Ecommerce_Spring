package com.judy.ecommerce.backend.dto.order;

public record OrderProductDTO(
        long id,
        String name,
        double pricePaid,
        int quantity
) {}
