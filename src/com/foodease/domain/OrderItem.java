package com.foodease.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record OrderItem(MenuItem menuItem, int quantity, String note) {
    public OrderItem(MenuItem menuItem, int quantity) {
        this(menuItem, quantity, "");
    }

    public OrderItem {
        Objects.requireNonNull(menuItem);
        note = note == null ? "" : note.trim();
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be at least 1");
    }

    public BigDecimal subtotal() {
        return menuItem.price().multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal unitPrice() { return menuItem.price(); }
}
