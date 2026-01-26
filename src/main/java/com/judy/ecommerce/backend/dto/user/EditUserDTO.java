package com.judy.ecommerce.backend.dto.user;

import jakarta.validation.constraints.Size;

import java.util.Optional;

public record EditUserDTO(
        Optional<String> lastName,
        Optional<String> firstName
) {}
