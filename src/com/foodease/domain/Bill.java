package com.foodease.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record Bill(BigDecimal subtotal, BigDecimal tax, BigDecimal serviceCharge, BigDecimal total,
                   boolean taxIncludedInPrices) {
    public Bill {
        Objects.requireNonNull(subtotal);
        Objects.requireNonNull(tax);
        Objects.requireNonNull(serviceCharge);
        Objects.requireNonNull(total);
    }
}
