package com.judy.ecommerce.backend.dto.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record NewProductDTO(
        @NotBlank @Max(255) String name,
        @NotBlank @Max(5000) String description,
        Optional<Integer> category,
        @Min(0) double price,
        @Min(0) int quantity,
        boolean disabled
) {}
