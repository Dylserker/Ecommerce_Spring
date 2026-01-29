package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.RoleEnum;
import com.judy.ecommerce.backend.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    // Return the User object based on the id
    // User must be active
    Optional<Users> findByIdAndDisabledFalse(long id);

    // Return the User object based on the email
    // Case insensitive and user must be active
    Optional<Users> findByEmailIgnoreCaseAndDisabledFalse(String email);

    // Check if the email exists
    // Case insensitive and user must be active
    boolean existsByEmailIgnoreCaseAndDisabledFalse(String email);

    // Count users by role
    // Should only take "ADMIN" as parameter to avoid deleting all admins
    int countUsersByRoleIs(RoleEnum role);

    List<Users> findAllBy(Pageable pageable);
    int countAllBy();

    // INITIALIZER //

    // Check if active user with specific role exists
    // Should ONLY be used to create an admin user by the initializer if there are none
    boolean existsByRoleAndDisabledFalse(@PathVariable RoleEnum role);

    // Return the User object based on the email
    // Used ONLY to get the default admin user in case its role have been changed to USER
    Optional<Users> findByEmailIgnoreCase(String email);
}
