package com.judy.ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "product")
    private List<Carts> carts;

    @OneToMany(mappedBy = "product")
    private List<OrderProducts> orderProducts;

    @Column(nullable = false)
    private String name;

    @Column(length = 5000, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity = 0;

    @Column(nullable = false)
    private boolean disabled;
}
