package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.entity.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigRepository extends JpaRepository<AppConfig, Long> {

    // Get config value by name
    Optional<AppConfig> findByConfigName(String name);
}
