package com.judy.ecommerce.backend.dto.search;

public record PaginationDTO(
        int currentPage,
        int maxPages,
        int currentPageItems,
        int maxItems
) {}
