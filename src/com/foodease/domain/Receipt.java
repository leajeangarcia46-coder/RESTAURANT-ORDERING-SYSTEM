package com.foodease.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record Receipt(String receiptNumber, Customer customer, OrderType orderType,
                      BigDecimal total, Payment payment, LocalDateTime issuedAt) {
    public Receipt {
        Objects.requireNonNull(receiptNumber);
        Objects.requireNonNull(customer);
        Objects.requireNonNull(orderType);
        Objects.requireNonNull(total);
        Objects.requireNonNull(payment);
        Objects.requireNonNull(issuedAt);
    }
}
