package com.foodease.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record MenuItem(String id, String name, MenuCategory category, BigDecimal price) {
    public MenuItem {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(category);
        Objects.requireNonNull(price);
        if (price.signum() < 0) throw new IllegalArgumentException("Price cannot be negative");
    }
}
