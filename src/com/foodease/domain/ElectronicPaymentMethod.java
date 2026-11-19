package com.foodease.domain;

import java.math.BigDecimal;

/** Card and wallet payments are recorded at the exact order total. */
public final class ElectronicPaymentMethod extends PaymentMethod {
    public ElectronicPaymentMethod(String name) {
        super(name);
    }

    @Override
    public Payment charge(BigDecimal total, BigDecimal amountTendered) {
        return new Payment(total, name(), total);
    }
}
