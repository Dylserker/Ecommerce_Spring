package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.OrderStatus;
import com.judy.ecommerce.backend.entity.Orders;
import com.judy.ecommerce.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findByIdAndUser(long id, Users user);

    List<Orders> findAllByUserOrderByOrderedAtDesc(Users user);

    List<Orders> findAllByUserAndStatusOrderByOrderedAtDesc(Users user, OrderStatus orderStatus);


    // ADMIN
    List<Orders> findAllByOrderByOrderedAtDesc();

    List<Orders> findAllByStatusOrderByOrderedAtDesc(OrderStatus status);
}
