package com.foodease.data;

import com.foodease.domain.MenuCategory;
import com.foodease.domain.MenuItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class InMemoryMenuRepository implements MenuRepository {
    private final List<MenuItem> items = List.of(
        new MenuItem("1", "Paracetamol 500 mg (10s)", MenuCategory.OTC_MEDICINES, new BigDecimal("25.00")),
        new MenuItem("2", "Oral Rehydration Salts", MenuCategory.OTC_MEDICINES, new BigDecimal("18.00")),
        new MenuItem("3", "Vitamin C 500 mg (10s)", MenuCategory.VITAMINS, new BigDecimal("35.00")),
        new MenuItem("4", "Adhesive Bandages (10s)", MenuCategory.FIRST_AID, new BigDecimal("22.00")),
        new MenuItem("5", "70% Isopropyl Alcohol (250 ml)", MenuCategory.FIRST_AID, new BigDecimal("48.00")),
        new MenuItem("6", "Surgical Face Masks (5s)", MenuCategory.PERSONAL_CARE, new BigDecimal("30.00"))
    );

    @Override public List<MenuItem> findAll() { return items; }
    @Override public Optional<MenuItem> findById(String id) {
        return items.stream().filter(item -> item.id().equals(id)).findFirst();
    }
}
