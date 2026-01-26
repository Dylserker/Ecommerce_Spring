package com.judy.ecommerce.backend.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PasswordDTO(
        @NotNull @Size(min=8, max=128) String currentPassword,
        @NotNull @Size(min=8, max=128) String newPassword
) {}
