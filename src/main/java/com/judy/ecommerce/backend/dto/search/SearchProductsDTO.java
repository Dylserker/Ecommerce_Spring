package com.judy.ecommerce.backend.dto.search;


import com.judy.ecommerce.backend.dto.product.ProductDTO;

import java.util.List;

public record SearchProductsDTO(
        List<ProductDTO> products,
        PaginationDTO pageInfos
) {}
