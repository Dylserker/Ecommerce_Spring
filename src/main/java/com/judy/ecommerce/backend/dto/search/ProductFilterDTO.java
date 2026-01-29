package com.judy.ecommerce.backend.dto.search;

import java.util.Optional;

public record FiltersDTO (
    Optional<Double> minPrice,
    Optional<Double> maxPrice,
    Optional<Integer> categoryId
) {}
