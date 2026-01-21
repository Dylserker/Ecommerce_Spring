package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.entity.Carts;
import com.judy.ecommerce.backend.entity.Products;
import com.judy.ecommerce.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Carts, Long> {
    // Get the user's cart content
    List<Carts> findAllByUser(Users user);

    // Get/check by product and user
    Optional<Carts> findByUserAndProduct(Users user, Products product);
    boolean existsByUserAndProduct(Users user, Products product);
}
