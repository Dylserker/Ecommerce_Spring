package com.judy.ecommerce.backend.dto.auth;

import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull String email,
        @NotNull String password
) {}
