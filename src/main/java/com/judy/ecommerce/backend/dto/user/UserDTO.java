package com.judy.ecommerce.backend.dto.user;

import java.time.LocalDateTime;

public record UserDTO(
        long id,
        String lastName,
        String firstName,
        String email,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean disabled
) {}
