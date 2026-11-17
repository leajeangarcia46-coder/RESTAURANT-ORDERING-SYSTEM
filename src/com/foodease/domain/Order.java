package com.foodease.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private final Customer customer;
    private final OrderType type;
    private final List<OrderItem> items = new ArrayList<>();

    public Order(Customer customer, OrderType type) {
        this.customer = Objects.requireNonNull(customer);
        this.type = Objects.requireNonNull(type);
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
    public List<OrderItem> items() { return List.copyOf(items); }
    public BigDecimal total() {
        return items.stream().map(OrderItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
