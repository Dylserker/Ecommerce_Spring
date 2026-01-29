package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.OrderStatus;
import com.judy.ecommerce.backend.entity.Orders;
import com.judy.ecommerce.backend.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findByIdAndUser(long id, Users user);

    List<Orders> findAllByUserOrderByOrderedAtDesc(Users user, Pageable pageable);
    int countAllByUser(Users user);

    List<Orders> findAllByUserAndStatusOrderByOrderedAtDesc(Users user, OrderStatus orderStatus, Pageable pageable);
    int countAllByUserAndStatus(Users user, OrderStatus orderStatus);


    // ADMIN //

    List<Orders> findAllByOrderByOrderedAtDesc(Pageable pageable);
    int countAllBy();

    List<Orders> findAllByStatusOrderByOrderedAtDesc(OrderStatus status, Pageable pageable);
    int countAllByStatus(OrderStatus status);
}
