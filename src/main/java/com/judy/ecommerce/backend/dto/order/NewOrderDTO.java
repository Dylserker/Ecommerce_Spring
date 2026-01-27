package com.judy.ecommerce.backend.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewOrderDTO (
        @NotBlank @Size(max=64) String deliveryLastName,
        @NotBlank @Size(max=64) String deliveryFirstName,
        @NotBlank @Size(max=255) String deliveryAddress,
        @NotNull @Size(min=8, max=19) String creditCardNumber,
        @NotNull @Size(min=4, max=4) String creditCardExp,
        @NotNull @Size(min=3, max=4) String creditCardCVV
) {}
