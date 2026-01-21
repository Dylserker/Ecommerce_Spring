package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    // Return the User object based on the id
    // User must be active
    Optional<Users> findByIdAndDisabledFalse(long id);

    // Return the User object based on the username
    // Case insensitive and user must be active
    Optional<Users> findByEmailIgnoreCaseAndDisabledFalse(String email);

    // Check if the email exists
    // Case insensitive and user must be active
    boolean existsByEmailIgnoreCaseAndDisabledFalse(String email);
}
