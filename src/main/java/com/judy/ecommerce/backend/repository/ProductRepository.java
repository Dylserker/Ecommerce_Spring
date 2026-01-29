package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.entity.Categories;
import com.judy.ecommerce.backend.entity.Products;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Products, Long> {

    // USER //

    /// Find product by id, check for disabled
    Optional<Products> findByIdAndDisabledFalse(long id);

    /// Get all products, check for disabled, with pagination system
    List<Products> findAllByDisabledFalse(Pageable pageable);

    /// Return a list of products matching with its exact name, check for disabled
    List<Products> findAllByNameAndDisabledFalse(String name, Pageable pageable);

    /// Return a list of products starting with the keyword, check for disabled
    List<Products> findAllByNameStartingWithAndDisabledFalse(String name, Pageable pageable);

    /// Return a list of products containing the keyword, check for disabled
    List<Products> findAllByNameContainsAndDisabledFalse(String name, Pageable pageable);

    /// Return a list of products in sale, doesn't check for disabled
    /// Sorted by highest sale
    List<Products> findAllBySalePercentGreaterThanAndDisabledFalseOrderBySalePercentDesc(double salePercent, Pageable pageable);

    // ADMIN //

    /// Find product by id, doesn't check for disabled
    Optional<Products> findById(long id);

    /// Return the full list of products, with pagination system
    List<Products> findAllBy(Pageable pageable);

    /// Return a list of products matching with its exact name, doesn't check for disabled
    List<Products> findAllByName(String name, Pageable pageable);

    /// Return a list of products starting with the keyword, doesn't check for disabled
    List<Products> findAllByNameStartingWith(String name, Pageable pageable);

    /// Return a list of products containing the keyword, doesn't check for disabled
    List<Products> findAllByNameContains(String name, Pageable pageable);

    List<Products> findByCategory(Categories category);

    /// Return a list of products in sale, doesn't check for disabled
    /// Sorted by highest sale
    @Query("SELECT p FROM Products p WHERE p.price <> p.salePrice ORDER BY p.salePercent DESC")
    List<Products> findAllBySalePercentGreaterThanOrderBySalePercentDesc(double salePercent, Pageable pageable);
}
