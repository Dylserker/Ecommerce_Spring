package com.judy.ecommerce.backend.dto.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record NewCategoryDTO(
        @NotBlank @Max(64) String name
) {}
