package com.foodease.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record Receipt(String receiptNumber, Customer customer, OrderType orderType,
                      BigDecimal subtotal, BigDecimal tax, BigDecimal serviceCharge,
                      BigDecimal total, boolean taxIncludedInPrices, Payment payment, LocalDateTime issuedAt) {
    public Receipt {
        Objects.requireNonNull(receiptNumber);
        Objects.requireNonNull(customer);
        Objects.requireNonNull(orderType);
        Objects.requireNonNull(subtotal);
        Objects.requireNonNull(tax);
        Objects.requireNonNull(serviceCharge);
        Objects.requireNonNull(total);
        Objects.requireNonNull(payment);
        Objects.requireNonNull(issuedAt);
    }
}
