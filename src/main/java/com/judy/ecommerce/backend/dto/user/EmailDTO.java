package com.judy.ecommerce.backend.dto.user;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

public record EmailDTO(
        @NotBlank @Size(max=255) String email
) {}
