package com.judy.ecommerce.backend.repository;

import com.judy.ecommerce.backend.entity.OrderProducts;
import com.judy.ecommerce.backend.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderProductRepository extends JpaRepository<OrderProducts, Long> {

    /// Return sum of products price by id
    @Query("""
        SELECT SUM(op.pricePaid * op.quantity) FROM OrderProducts op
        WHERE op.order = :order
    """)
    double getPriceSumByOrder(Orders order);
}
