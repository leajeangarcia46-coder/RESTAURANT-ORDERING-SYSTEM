package com.foodease.data;

import com.foodease.domain.MenuItem;
import java.util.List;
import java.util.Optional;

public interface MenuRepository {
    List<MenuItem> findAll();
    Optional<MenuItem> findById(String id);
}
