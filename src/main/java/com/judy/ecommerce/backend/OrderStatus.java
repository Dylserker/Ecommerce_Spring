package com.judy.ecommerce.backend;

public enum OrderStatus {
    NOT_CONFIRMED("NOT_CONFIRMED"),
    CONFIRMED("CONFIRMED"),
    PREPARED("PREPARED"),
    DELIVERED("DELIVERED");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return this.status;
    }
}
