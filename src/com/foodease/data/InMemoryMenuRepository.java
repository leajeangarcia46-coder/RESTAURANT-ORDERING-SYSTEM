package com.foodease.data;

import com.foodease.domain.MenuCategory;
import com.foodease.domain.MenuItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class InMemoryMenuRepository implements MenuRepository {
    private final List<MenuItem> items = List.of(
        new MenuItem("1", "Classic Burger", MenuCategory.BURGERS, new BigDecimal("8.99")),
        new MenuItem("2", "Cheeseburger", MenuCategory.BURGERS, new BigDecimal("9.99")),
        new MenuItem("3", "French Fries", MenuCategory.SIDES, new BigDecimal("3.49")),
        new MenuItem("4", "Soft Drink", MenuCategory.DRINKS, new BigDecimal("1.99")),
        new MenuItem("5", "Chocolate Sundae", MenuCategory.DESSERTS, new BigDecimal("4.25"))
    );

    @Override public List<MenuItem> findAll() { return items; }
    @Override public Optional<MenuItem> findById(String id) {
        return items.stream().filter(item -> item.id().equals(id)).findFirst();
    }
}
