package com.foodease.application;

import com.foodease.data.MenuRepository;
import com.foodease.domain.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderService {
    private final MenuRepository menuRepository;
    private final PricingPolicy pricingPolicy;

    public OrderService(MenuRepository menuRepository) {
        this(menuRepository, PricingPolicy.philippinesDemoDefaults());
    }
    public OrderService(MenuRepository menuRepository, PricingPolicy pricingPolicy) {
        this.menuRepository = java.util.Objects.requireNonNull(menuRepository);
        this.pricingPolicy = java.util.Objects.requireNonNull(pricingPolicy);
    }
    public List<MenuItem> getMenu() { return menuRepository.findAll(); }
    public Bill calculateBill(Order order) { return pricingPolicy.calculate(order); }
    public MenuItem getMenuItem(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + id));
    }
    public Order createOrder(String customerName, OrderType type) {
        return new Order(new Customer(customerName), type);
    }
    public Order createOrder(String customerName, OrderType type, String tableNumber) {
        return new Order(new Customer(customerName), type, tableNumber);
    }
    public Receipt checkout(Order order, String method) {
        return checkout(order, method, calculateBill(order).total());
    }

    public Receipt checkout(Order order, String method, java.math.BigDecimal amountTendered) {
        if (order.items().isEmpty()) throw new IllegalStateException("Add at least one item before checkout");
        String normalizedMethod = method.trim().toLowerCase(java.util.Locale.ROOT);
        if (!normalizedMethod.equals("cash") && !normalizedMethod.equals("card")
                && !normalizedMethod.equals("wallet")) {
            throw new IllegalArgumentException("Choose cash, card, or wallet");
        }
        Bill bill = calculateBill(order);
        if (!normalizedMethod.equals("cash")) amountTendered = bill.total();
        Payment payment = new Payment(bill.total(), normalizedMethod, amountTendered);
        return new Receipt(UUID.randomUUID().toString().substring(0, 8).toUpperCase(), order.customer(),
                order.type(), bill.subtotal(), bill.tax(), bill.serviceCharge(), bill.total(),
                bill.taxIncludedInPrices(), payment, LocalDateTime.now());
    }
}
