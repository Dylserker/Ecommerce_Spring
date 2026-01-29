package com.judy.ecommerce.backend.dto.filter;

import java.util.Optional;

public record ProductFilterDTO(
    Optional<Double> minPrice,
    Optional<Double> maxPrice,
    Optional<Integer> categoryId
) {}
