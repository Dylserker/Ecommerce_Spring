package com.judy.ecommerce.backend;

public enum RoleEnum {
    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    private RoleEnum(String role) {
        this.role = role;
    }

    public String toString() {
        return this.role;
    }
}
