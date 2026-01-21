package com.judy.ecommerce.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank @Size(max=64) String lastName,
        @NotBlank @Size(max=64) String firstName,
        @NotBlank @Size(max=255) String email,
        @NotBlank @Size(min=8, max=128) String password
) {}
