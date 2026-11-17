package com.foodease.console;

import com.foodease.domain.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ConsoleOutput {
    private final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-PH"));

    public void showWelcome() {
        System.out.println("============================================================");
        System.out.println("                    MEDICARE PHARMACY");
        System.out.println("                 CUSTOMER ORDER SYSTEM");
        System.out.println("============================================================");
        System.out.println("Sample catalog and demo prices | All prices in Philippine pesos");
    }

    public void showOrderActions() {
        System.out.println("\n---------------------- ORDER OPTIONS -----------------------");
        System.out.println("A) Add product    V) View order    R) Remove product");
        System.out.println("C) Checkout       X) Cancel");
    }

    public void showMenu(Iterable<MenuItem> items) {
        System.out.println("\n======================= PRODUCT CATALOG ====================");
        System.out.printf("%-4s %-36s %-18s %10s%n", "No.", "Product", "Category", "Price");
        System.out.println("------------------------------------------------------------");
        for (MenuItem item : items)
            System.out.printf("%-4s %-36s %-18s %10s%n", item.id(), item.name(),
                    categoryLabel(item.category()), currency.format(item.price()));
        System.out.println("============================================================");
        System.out.println("Demo catalog only. Confirm product suitability with a pharmacist.");
    }

    public void showReceipt(Receipt receipt, Order order) {
        System.out.println("\n============================================================");
        System.out.println("                   MEDICARE PHARMACY");
        System.out.println("                    SALES RECEIPT");
        System.out.println("Receipt No.: " + receipt.receiptNumber());
        System.out.println("Customer: " + receipt.customer().name());
        System.out.println("Purchase: " + purchaseTypeLabel(receipt.orderType()));
        System.out.println("Issued: " + receipt.issuedAt().toString().replace('T', ' '));
        System.out.println("------------------------------------------------------------");
        for (OrderItem item : order.items())
            System.out.printf("%-36s x%-3d %10s%n", item.menuItem().name(), item.quantity(), currency.format(item.subtotal()));
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-40s %10s%n", "TOTAL", currency.format(receipt.total()));
        System.out.println("Payment: " + capitalize(receipt.payment().method()));
        if (receipt.payment().method().equals("cash")) {
            System.out.printf("%-40s %10s%n", "Cash received", currency.format(receipt.payment().amountTendered()));
            System.out.printf("%-40s %10s%n", "Change", currency.format(receipt.payment().changeDue()));
        }
        System.out.println("============================================================");
    }

    public void showCart(Order order) {
        System.out.println("\n------------------------- YOUR ORDER -----------------------");
        if (order.items().isEmpty()) {
            System.out.println("No products have been added yet.");
            return;
        }
        int number = 1;
        for (OrderItem item : order.items()) {
            System.out.printf("%d. %-36s x%d  %s%n", number++, item.menuItem().name(),
                    item.quantity(), currency.format(item.subtotal()));
        }
        showTotal(order.total());
    }

    public void showTotal(java.math.BigDecimal total) {
        System.out.println("Current total: " + currency.format(total));
    }

    private String categoryLabel(MenuCategory category) {
        return switch (category) {
            case OTC_MEDICINES -> "OTC Medicines";
            case VITAMINS -> "Vitamins";
            case FIRST_AID -> "First Aid";
            case PERSONAL_CARE -> "Personal Care";
        };
    }

    private String purchaseTypeLabel(OrderType type) {
        return type == OrderType.DINE_IN ? "In-store" : "Pickup";
    }

    private String capitalize(String value) {
        if (value.isEmpty()) return value;
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1);
    }

    public void print(String message) { System.out.println(message); }
}
