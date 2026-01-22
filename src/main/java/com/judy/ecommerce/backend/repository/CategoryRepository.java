package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Categories, Long> {
    // Get by id
    Optional<Categories> findById(long id);
}
