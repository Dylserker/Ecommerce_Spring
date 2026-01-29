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
    int countAllByDisabledFalse();

    /// Return a list of products matching with its exact name, check for disabled
    //List<Products> findAllByNameAndDisabledFalse(String name, Pageable pageable);

    /// Return a list of products starting with the keyword, check for disabled
    //List<Products> findAllByNameStartingWithAndDisabledFalse(String name, Pageable pageable);

    /// Return a list of products containing the keyword, check for disabled
    //List<Products> findAllByNameContainsAndDisabledFalse(String name, Pageable pageable);

    /// Handle search, exclude disabled items
    /// Use a negative number if not present
    @Query("""
        SELECT p
        FROM Products p
        WHERE (p.name LIKE %:name%)
            AND (:minPrice < 0 OR p.salePrice >= :minPrice)
            AND (:maxPrice < 0 OR p.salePrice <= :maxPrice)
            AND (:categoryId < 0 OR p.category.id = :categoryId)
            AND p.disabled = FALSE
    """)
    List<Products> findAllByNameLikeAndSearchOptionsAndDisabledFalse(
            String name,
            double minPrice,
            double maxPrice,
            int categoryId,
            Pageable pageable
    );

    @Query("""
        SELECT COUNT(p)
        FROM Products p
        WHERE (p.name LIKE %:name%)
            AND (:minPrice < 0 OR p.salePrice >= :minPrice)
            AND (:maxPrice < 0 OR p.salePrice <= :maxPrice)
            AND (:categoryId < 0 OR p.category.id = :categoryId)
            AND p.disabled = FALSE
    """)
    int countAllByNameLikeAndSearchOptionsAndDisabledFalse(
            String name,
            double minPrice,
            double maxPrice,
            int categoryId
    );

    /// Return a list of products in sale, doesn't check for disabled
    /// Sorted by highest sale
    List<Products> findAllBySalePercentGreaterThanAndDisabledFalseOrderBySalePercentDesc(double salePercent, Pageable pageable);
    int countAllBySalePercentGreaterThanAndDisabledFalse(double salePercent);

    // ADMIN //

    /// Find product by id, doesn't check for disabled
    Optional<Products> findById(long id);

    /// Return the full list of products, with pagination system
    List<Products> findAllBy(Pageable pageable);
    int countAllBy();

    /// Return a list of products matching with its exact name, doesn't check for disabled
    //List<Products> findAllByName(String name, Pageable pageable);

    /// Return a list of products starting with the keyword, doesn't check for disabled
    //List<Products> findAllByNameStartingWith(String name, Pageable pageable);

    /// Return a list of products containing the keyword, doesn't check for disabled
    //List<Products> findAllByNameContains(String name, Pageable pageable);

    List<Products> findByCategory(Categories category);

    /// Return/count a list of products in sale, doesn't check for disabled
    /// Sorted by highest sale
    List<Products> findAllBySalePercentGreaterThanOrderBySalePercentDesc(double salePercent, Pageable pageable);
    int countAllBySalePercentGreaterThan(double salePercent);

    /// Handle search, include disabled items
    /// Use a negative number if not present
    @Query("""
        SELECT p
        FROM Products p
        WHERE (p.name LIKE %:name%)
            AND (:minPrice < 0 OR p.salePrice >= :minPrice)
            AND (:maxPrice < 0 OR p.salePrice <= :maxPrice)
            AND (:categoryId < 0 OR p.category.id = :categoryId)
    """)
    List<Products> findAllByNameLikeAndSearchOptions(
            String name,
            double minPrice,
            double maxPrice,
            int categoryId,
            Pageable pageable
    );

    @Query("""
        SELECT COUNT(p)
        FROM Products p
        WHERE (p.name LIKE %:name%)
            AND (:minPrice < 0 OR p.salePrice >= :minPrice)
            AND (:maxPrice < 0 OR p.salePrice <= :maxPrice)
            AND (:categoryId < 0 OR p.category.id = :categoryId)
    """)
    int countAllByNameLikeAndSearchOptions(
            String name,
            double minPrice,
            double maxPrice,
            int categoryId
    );
}
