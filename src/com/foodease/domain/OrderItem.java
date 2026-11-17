package com.foodease.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record OrderItem(MenuItem menuItem, int quantity) {
    public OrderItem {
        Objects.requireNonNull(menuItem);
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be at least 1");
    }

    public BigDecimal subtotal() {
        return menuItem.price().multiply(BigDecimal.valueOf(quantity));
    }
}
