package com.foodease.domain;

import java.util.Objects;

public record Customer(String name) {
    public Customer {
        Objects.requireNonNull(name);
        name = name.trim();
        if (name.isEmpty()) throw new IllegalArgumentException("Customer name is required");
    }
}
