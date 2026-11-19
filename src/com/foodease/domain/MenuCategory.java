package com.foodease.domain;

public enum MenuCategory {
    APPETIZERS("Appetizers"),
    MAIN_COURSES("Main Courses"),
    SIDES("Sides"),
    BEVERAGES("Beverages"),
    DESSERTS("Desserts");

    private final String displayName;

    MenuCategory(String displayName) { this.displayName = displayName; }

    public String displayName() { return displayName; }
}
