package com.foodease.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private final Customer customer;
    private final OrderType type;
    private final String tableNumber;
    private final List<OrderItem> items = new ArrayList<>();

    public Order(Customer customer, OrderType type) {
        this(customer, type, type == OrderType.DINE_IN ? "Walk-in" : "");
    }

    public Order(Customer customer, OrderType type, String tableNumber) {
        this.customer = Objects.requireNonNull(customer);
        this.type = Objects.requireNonNull(type);
        this.tableNumber = tableNumber == null ? "" : tableNumber.trim();
        if (type == OrderType.DINE_IN && this.tableNumber.isEmpty()) {
            throw new IllegalArgumentException("A table number is required for dine-in orders");
        }
    }

    public void addItem(MenuItem item, int quantity) {
        Objects.requireNonNull(item);
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be at least 1");
        for (int index = 0; index < items.size(); index++) {
            OrderItem current = items.get(index);
            if (current.menuItem().id().equals(item.id())) {
                items.set(index, new OrderItem(item, current.quantity() + quantity));
                return;
            }
        }
        items.add(new OrderItem(item, quantity));
    }

    public void removeItem(int itemNumber) {
        if (itemNumber < 1 || itemNumber > items.size()) {
            throw new IllegalArgumentException("Cart item number is out of range");
        }
        items.remove(itemNumber - 1);
    }

    public Customer customer() { return customer; }
    public OrderType type() { return type; }
    public String tableNumber() { return tableNumber; }
    public List<OrderItem> items() { return List.copyOf(items); }
    public BigDecimal total() {
        return items.stream().map(OrderItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
