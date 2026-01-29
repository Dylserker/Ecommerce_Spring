package com.judy.ecommerce.backend;

import java.util.Objects;
import java.util.stream.Stream;

public enum OrderStatus {
    CONFIRMED("CONFIRMED"),
    PREPARED("PREPARED"),
    DELIVERED("DELIVERED"),
    CANCELED("CANCELED");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return this.status;
    }

    public boolean isCancellable() {
        return Objects.equals(this.status, CONFIRMED.status);
    }

    public static boolean contains(String test) {
        for (OrderStatus o : OrderStatus.values()) {
            if (o.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
}
