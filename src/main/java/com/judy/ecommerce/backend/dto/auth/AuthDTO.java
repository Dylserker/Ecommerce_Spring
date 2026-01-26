package com.judy.ecommerce.backend.dto.auth;

import java.util.Date;

public record AuthDTO(
        String token,
        Date expirationDate
) {}
