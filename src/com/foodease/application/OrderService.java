package com.foodease.application;

import com.foodease.data.MenuRepository;
import com.foodease.domain.Bill;
import com.foodease.domain.Customer;
import com.foodease.domain.MenuItem;
import com.foodease.domain.Order;
import com.foodease.domain.OrderType;
import com.foodease.domain.Payment;
import com.foodease.domain.Receipt;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/** Coordinates menu access, order creation, billing, and checkout. */
public class OrderService {
    private final MenuRepository menuRepository;
    private final PricingPolicy pricingPolicy;

    public OrderService(MenuRepository menuRepository) {
        this(menuRepository, PricingPolicy.philippinesDemoDefaults());
    }

    public OrderService(MenuRepository menuRepository, PricingPolicy pricingPolicy) {
        this.menuRepository = Objects.requireNonNull(menuRepository);
        this.pricingPolicy = Objects.requireNonNull(pricingPolicy);
    }

    public List<MenuItem> getMenu() {
        return menuRepository.findAll();
    }

    public MenuItem getMenuItem(String id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + id));
    }

    public Bill calculateBill(Order order) {
        return pricingPolicy.calculate(order);
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

    public Receipt checkout(Order order, String method, BigDecimal amountTendered) {
        Objects.requireNonNull(order);
        if (order.items().isEmpty()) {
            throw new IllegalStateException("Add at least one item before checkout");
        }

        Bill bill = calculateBill(order);
        Payment payment = Payment.create(bill.total(), method, amountTendered);
        String receiptNumber = UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);

        return new Receipt(receiptNumber, order.customer(), order.type(), bill.subtotal(), bill.tax(),
                bill.serviceCharge(), bill.total(), bill.taxIncludedInPrices(), payment, LocalDateTime.now());
    }

}
