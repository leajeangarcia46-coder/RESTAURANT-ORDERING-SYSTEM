package com.foodease.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record Payment(BigDecimal amount, String method, BigDecimal amountTendered) {
    public Payment(BigDecimal amount, String method) {
        this(amount, method, amount);
    }

    public Payment {
        Objects.requireNonNull(amount);
        Objects.requireNonNull(method);
        Objects.requireNonNull(amountTendered);
        if (amount.signum() < 0) throw new IllegalArgumentException("Payment cannot be negative");
        if (amountTendered.compareTo(amount) < 0) throw new IllegalArgumentException("Payment is less than the order total");
        method = method.trim();
        if (method.isEmpty()) throw new IllegalArgumentException("Payment method is required");
    }

    public BigDecimal changeDue() {
        return amountTendered.subtract(amount);
    }
}
