package com.judy.ecommerce.backend.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewCategoryDTO(
        @NotBlank @Size(max=64) String name
) {}
