package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {

}
