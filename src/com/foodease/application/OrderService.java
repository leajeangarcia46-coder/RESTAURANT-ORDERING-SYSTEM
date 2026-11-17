package com.foodease.application;

import com.foodease.data.MenuRepository;
import com.foodease.domain.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderService {
    private final MenuRepository menuRepository;

    public OrderService(MenuRepository menuRepository) { this.menuRepository = menuRepository; }
    public List<MenuItem> getMenu() { return menuRepository.findAll(); }
    public MenuItem getMenuItem(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + id));
    }
    public Order createOrder(String customerName, OrderType type) {
        return new Order(new Customer(customerName), type);
    }
    public Receipt checkout(Order order, String method) {
        return checkout(order, method, order.total());
    }

    public Receipt checkout(Order order, String method, java.math.BigDecimal amountTendered) {
        if (order.items().isEmpty()) throw new IllegalStateException("Add at least one item before checkout");
        String normalizedMethod = method.trim().toLowerCase();
        if (!normalizedMethod.equals("cash") && !normalizedMethod.equals("card")) {
            throw new IllegalArgumentException("Choose cash or card");
        }
        if (normalizedMethod.equals("card")) amountTendered = order.total();
        Payment payment = new Payment(order.total(), normalizedMethod, amountTendered);
        return new Receipt(UUID.randomUUID().toString().substring(0, 8).toUpperCase(), order.customer(),
                order.type(), order.total(), payment, LocalDateTime.now());
    }
}
