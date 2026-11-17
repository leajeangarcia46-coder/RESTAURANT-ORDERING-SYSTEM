package com.foodease.data;

import com.foodease.domain.MenuCategory;
import com.foodease.domain.MenuItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class InMemoryMenuRepository implements MenuRepository {
    private final List<MenuItem> items = List.of(
        new MenuItem("1", "Lumpiang Shanghai (6 pcs)", MenuCategory.APPETIZERS, new BigDecimal("75.00")),
        new MenuItem("2", "Chicken Adobo", MenuCategory.MAIN_COURSES, new BigDecimal("120.00")),
        new MenuItem("3", "Pork Sinigang", MenuCategory.MAIN_COURSES, new BigDecimal("150.00")),
        new MenuItem("4", "Pancit Canton", MenuCategory.MAIN_COURSES, new BigDecimal("110.00")),
        new MenuItem("5", "Garlic Rice", MenuCategory.SIDES, new BigDecimal("25.00")),
        new MenuItem("6", "French Fries", MenuCategory.SIDES, new BigDecimal("65.00")),
        new MenuItem("7", "Iced Tea", MenuCategory.BEVERAGES, new BigDecimal("35.00")),
        new MenuItem("8", "Halo-Halo", MenuCategory.DESSERTS, new BigDecimal("95.00"))
    );

    @Override public List<MenuItem> findAll() { return items; }
    @Override public Optional<MenuItem> findById(String id) {
        return items.stream().filter(item -> item.id().equals(id)).findFirst();
    }
}
