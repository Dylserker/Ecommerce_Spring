package com.judy.ecommerce.backend.dto.search;

import com.judy.ecommerce.backend.dto.order.OrderDTO;

import java.util.List;

public record SearchOrdersDTO(
        List<OrderDTO> orders,
        PaginationDTO pageInfos
) {}
