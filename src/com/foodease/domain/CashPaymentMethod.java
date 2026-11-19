package com.foodease.domain;

import java.math.BigDecimal;

/** Cash records the amount received so the receipt can calculate change. */
public final class CashPaymentMethod extends PaymentMethod {
    public CashPaymentMethod() {
        super("cash");
    }

    @Override
    public Payment charge(BigDecimal total, BigDecimal amountTendered) {
        return new Payment(total, name(), amountTendered);
    }
}
