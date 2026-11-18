package com.foodease.application;

import com.foodease.domain.Bill;
import com.foodease.domain.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Configurable billing rates. Default assumes VAT-inclusive menu prices and no service charge. */
public class PricingPolicy {
    private static final BigDecimal ONE = BigDecimal.ONE;
    private final BigDecimal taxRate;
    private final BigDecimal serviceChargeRate;
    private final boolean pricesIncludeTax;

    public PricingPolicy(BigDecimal taxRate, BigDecimal serviceChargeRate, boolean pricesIncludeTax) {
        this.taxRate = requireRate(taxRate, "Tax rate");
        this.serviceChargeRate = requireRate(serviceChargeRate, "Service charge rate");
        this.pricesIncludeTax = pricesIncludeTax;
    }

    public static PricingPolicy philippinesDemoDefaults() {
        return new PricingPolicy(new BigDecimal("0.12"), BigDecimal.ZERO, true);
    }

    public Bill calculate(Order order) {
        BigDecimal menuSubtotal = order.total();
        BigDecimal tax;
        BigDecimal total;
        if (pricesIncludeTax && taxRate.signum() > 0) {
            tax = menuSubtotal.multiply(taxRate).divide(ONE.add(taxRate), 2, RoundingMode.HALF_UP);
            total = menuSubtotal;
        } else {
            tax = menuSubtotal.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
            total = menuSubtotal.add(tax);
        }
        BigDecimal serviceCharge = menuSubtotal.multiply(serviceChargeRate).setScale(2, RoundingMode.HALF_UP);
        total = total.add(serviceCharge).setScale(2, RoundingMode.HALF_UP);
        return new Bill(menuSubtotal, tax, serviceCharge, total, pricesIncludeTax);
    }

    private BigDecimal requireRate(BigDecimal rate, String label) {
        Objects.requireNonNull(rate);
        if (rate.signum() < 0 || rate.compareTo(ONE) > 0) {
            throw new IllegalArgumentException(label + " must be between 0 and 1");
        }
        return rate;
    }
}
