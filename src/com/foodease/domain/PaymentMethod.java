package com.foodease.domain;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

/** Abstraction for payment-specific checkout rules. */
public abstract class PaymentMethod {
    private final String name;

    protected PaymentMethod(String name) {
        String normalized = Objects.requireNonNull(name).trim().toLowerCase(Locale.ROOT);
        if (!normalized.equals("cash") && !normalized.equals("card") && !normalized.equals("wallet")) {
            throw new IllegalArgumentException("Choose cash, card, or wallet");
        }
        this.name = normalized;
    }

    public static PaymentMethod from(String choice) {
        String method = choice == null ? "" : choice.trim().toLowerCase(Locale.ROOT);
        return switch (method) {
            case "cash" -> new CashPaymentMethod();
            case "card", "wallet" -> new ElectronicPaymentMethod(method);
            default -> throw new IllegalArgumentException("Choose cash, card, or wallet");
        };
    }

    public String name() { return name; }

    public abstract Payment charge(BigDecimal total, BigDecimal amountTendered);
}
