package com.judy.ecommerce.backend.dto.search;

import com.judy.ecommerce.backend.dto.user.UserDTO;

import java.util.List;

public record SearchUsersDTO(
        List<UserDTO> users,
        PaginationDTO pageInfos
) {}
