package com.judy.ecommerce.backend.dto.order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
    long id,
    long userId,
    List<OrderProductDTO> products,
    double totalPrice,
    double shippingFees,
    String deliveryLastName,
    String deliveryFirstName,
    String deliveryAddress,
    LocalDateTime orderedAt
) {}
