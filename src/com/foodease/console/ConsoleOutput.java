package com.foodease.console;

import com.foodease.domain.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ConsoleOutput {
    private final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
    public void showMenu(Iterable<MenuItem> items) {
        System.out.println("\n--- FoodEase Menu ---");
        for (MenuItem item : items)
            System.out.printf("%s. %-22s %-10s %s%n", item.id(), item.name(), item.category(), currency.format(item.price()));
    }
    public void showReceipt(Receipt receipt, Order order) {
        System.out.println("\n--- Receipt " + receipt.receiptNumber() + " ---");
        System.out.println("Customer: " + receipt.customer().name() + " | " + receipt.orderType());
        System.out.println("Issued: " + receipt.issuedAt().toString().replace('T', ' '));
        for (OrderItem item : order.items())
            System.out.printf("%-22s x%d  %s%n", item.menuItem().name(), item.quantity(), currency.format(item.subtotal()));
        System.out.println("Total: " + currency.format(receipt.total()));
        System.out.println("Paid via: " + receipt.payment().method());
        if (receipt.payment().method().equals("cash")) {
            System.out.println("Cash received: " + currency.format(receipt.payment().amountTendered()));
            System.out.println("Change: " + currency.format(receipt.payment().changeDue()));
        }
    }

    public void showCart(Order order) {
        System.out.println("\n--- Your Order ---");
        if (order.items().isEmpty()) {
            System.out.println("Your order is empty.");
            return;
        }
        int number = 1;
        for (OrderItem item : order.items()) {
            System.out.printf("%d. %-22s x%d  %s%n", number++, item.menuItem().name(),
                    item.quantity(), currency.format(item.subtotal()));
        }
        System.out.println("Total: " + currency.format(order.total()));
    }

    public void showTotal(java.math.BigDecimal total) {
        System.out.println("Current total: " + currency.format(total));
    }
    public void print(String message) { System.out.println(message); }
}
