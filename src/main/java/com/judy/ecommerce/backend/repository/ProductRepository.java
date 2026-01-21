package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Products, Long> {
    // Find product by id
    Optional<Products> findById(long id);

    // Return a list of products matching with its exact name
    List<Products> findAllByName(String name);

    // Return a list of products starting with the keyword
    List<Products> findAllByNameStartingWith(String name);

    // Return a list of products containing the keyword
    List<Products> findAllByNameContains(String name);
}
