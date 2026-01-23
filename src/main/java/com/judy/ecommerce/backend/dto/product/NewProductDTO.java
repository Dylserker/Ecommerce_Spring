package com.judy.ecommerce.backend.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record NewProductDTO(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 5000) String description,
        Optional<Integer> category,
        @Min(0) double price,
        @Min(0) int quantity,
        boolean disabled
) {}
