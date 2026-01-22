package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Products, Long> {
    // USER //

    /// Find product by id, check for disabled
    Optional<Products> findByIdAndDisabledFalse(long id);

    /// Get all products, check for disabled
    List<Products> findAllByDisabledFalse();

    /// Return a list of products matching with its exact name, check for disabled
    List<Products> findAllByNameAndDisabledFalse(String name);

    /// Return a list of products starting with the keyword, check for disabled
    List<Products> findAllByNameStartingWithAndDisabledFalse(String name);

    /// Return a list of products containing the keyword, check for disabled
    List<Products> findAllByNameContainsAndDisabledFalse(String name);


    // ADMIN //

    /// Find product by id, doesn't check for disabled
    Optional<Products> findById(long id);

    // findAll() is already defined

    /// Return a list of products matching with its exact name, doesn't check for disabled
    List<Products> findAllByName(String name);

    /// Return a list of products starting with the keyword, doesn't check for disabled
    List<Products> findAllByNameStartingWith(String name);

    /// Return a list of products containing the keyword, doesn't check for disabled
    List<Products> findAllByNameContains(String name);
}
