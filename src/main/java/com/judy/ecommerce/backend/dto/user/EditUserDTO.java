package com.judy.ecommerce.backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record EditUserDTO(
        @NotBlank @Size(max=64) Optional<String> lastName,
        @NotBlank @Size(max=64) Optional<String> firstName
) {}
