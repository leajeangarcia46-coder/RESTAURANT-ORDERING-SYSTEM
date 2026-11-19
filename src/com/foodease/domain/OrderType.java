package com.foodease.domain;

public enum OrderType {
    DINE_IN("Dine-in"), TAKEOUT("Takeout");

    private final String displayName;

    OrderType(String displayName) { this.displayName = displayName; }

    public String displayName() { return displayName; }
}
