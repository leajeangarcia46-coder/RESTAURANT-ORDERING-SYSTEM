package com.foodease.console;

import com.foodease.domain.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ConsoleOutput {
    public static final int CONSOLE_WIDTH = 60;
    private static final int MENU_NAME_WIDTH = 34;
    private static final int CATEGORY_WIDTH = 16;
    private static final int PRICE_WIDTH = 14;
    private final NumberFormat currency = NumberFormat.getNumberInstance(Locale.US);

    public ConsoleOutput() {
        currency.setMinimumFractionDigits(2);
        currency.setMaximumFractionDigits(2);
    }

    public void showWelcome() {
        System.out.println("============================================================");
        printCentered("FOODEASE RESTAURANT");
        printCentered("ORDERING & CHECKOUT");
        System.out.println("============================================================");
        printFrameBorder();
        printFrameLine("Welcome! Start a new dine-in or takeout order.");
    }

    public void closeOrderSetup() {
        printFrameBorder();
    }

    public void printFrameMessage(String message) {
        printFrameLine(message);
    }

    public void showOrderActions() {
        System.out.println();
        printFrameBorder();
        printFrameLine("ORDER OPTIONS");
        printFrameBorder();
        printFrameLine("1) Add item     2) View order     3) Remove item");
        printFrameLine("4) Checkout     5) Cancel");
        printFrameBorder();
    }

    public void showMenu(Iterable<MenuItem> items) {
        printMenuBorder();
        printMenuTitle("RESTAURANT MENU");
        printMenuBorder();
        System.out.printf("| %-3s | %-" + MENU_NAME_WIDTH + "s | %-" + CATEGORY_WIDTH + "s | %" + PRICE_WIDTH + "s |%n",
                "No.", "Menu item", "Category", "Price");
        printMenuBorder();
        for (MenuItem item : items)
            System.out.printf("| %-3s | %-" + MENU_NAME_WIDTH + "s | %-" + CATEGORY_WIDTH + "s | %" + PRICE_WIDTH + "s |%n",
                    item.id(), item.name(), categoryLabel(item.category()), formatCurrency(item.price()));
        printMenuBorder();
    }

    public void showReceipt(Receipt receipt, Order order) {
        System.out.println("\n============================================================");
        System.out.println("                  FOODEASE RESTAURANT");
        System.out.println("                       RECEIPT");
        System.out.println("Order No.: " + receipt.receiptNumber());
        System.out.println("Customer: " + receipt.customer().name());
        System.out.println("Order type: " + orderTypeLabel(receipt.orderType()));
        if (receipt.orderType() == OrderType.DINE_IN) {
            System.out.println("Table: " + order.tableNumber());
        }
        if (!order.specialInstructions().isBlank()) {
            System.out.println("Kitchen instructions: " + order.specialInstructions());
        }
        System.out.println("Issued: " + receipt.issuedAt().toString().replace('T', ' '));
        System.out.println("------------------------------------------------------------");
        for (OrderItem item : order.items())
            System.out.printf("%-36s x%-3d %10s%n", item.menuItem().name(), item.quantity(), formatCurrency(item.subtotal()));
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-40s %10s%n", "TOTAL", formatCurrency(receipt.total()));
        System.out.println("Payment: " + capitalize(receipt.payment().method()));
        if (receipt.payment().method().equals("cash")) {
            System.out.printf("%-40s %10s%n", "Cash received", formatCurrency(receipt.payment().amountTendered()));
            System.out.printf("%-40s %10s%n", "Change", formatCurrency(receipt.payment().changeDue()));
        }
        System.out.println("============================================================");
    }

    public void showCart(Order order) {
        System.out.println();
        printFrameBorder();
        printFrameLine("CURRENT ORDER");
        printFrameBorder();
        System.out.println("Type: " + orderTypeLabel(order.type()));
        if (order.type() == OrderType.DINE_IN) {
            System.out.println("Table: " + order.tableNumber());
        }
        if (!order.specialInstructions().isBlank()) {
            System.out.println("Kitchen instructions: " + order.specialInstructions());
        }
        if (order.items().isEmpty()) {
            System.out.println("No items have been added yet.");
            printFrameBorder();
            return;
        }
        System.out.println("------------------------------------------------------------");
        int number = 1;
        for (OrderItem item : order.items()) {
            System.out.printf("%d. %-36s x%d  %s%n", number++, item.menuItem().name(),
                    item.quantity(), formatCurrency(item.subtotal()));
        }
        System.out.println("------------------------------------------------------------");
        showTotal(order.total());
        printFrameBorder();
    }

    public void showTotal(java.math.BigDecimal total) {
        System.out.println("Current total: " + formatCurrency(total));
    }

    private String categoryLabel(MenuCategory category) {
        return switch (category) {
            case APPETIZERS -> "Appetizers";
            case MAIN_COURSES -> "Main Courses";
            case SIDES -> "Sides";
            case BEVERAGES -> "Beverages";
            case DESSERTS -> "Desserts";
        };
    }

    private String orderTypeLabel(OrderType type) {
        return type == OrderType.DINE_IN ? "Dine-in" : "Takeout";
    }

    private String capitalize(String value) {
        if (value.isEmpty()) return value;
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1);
    }

    private void printCentered(String text) {
        int leftPadding = Math.max(0, (CONSOLE_WIDTH - text.length()) / 2);
        System.out.println(" ".repeat(leftPadding) + text);
    }

    private void printFrameBorder() {
        System.out.println("+" + "-".repeat(CONSOLE_WIDTH - 2) + "+");
    }

    private void printFrameLine(String text) {
        int insideWidth = CONSOLE_WIDTH - 2;
        int visibleLength = Math.min(text.length(), insideWidth);
        String visibleText = text.substring(0, visibleLength);
        int leftPadding = (insideWidth - visibleLength) / 2;
        int rightPadding = insideWidth - visibleLength - leftPadding;
        System.out.println("|" + " ".repeat(leftPadding) + visibleText
                + " ".repeat(rightPadding) + "|");
    }

    private void printMenuBorder() {
        System.out.println("+-----+" + "-".repeat(MENU_NAME_WIDTH + 2)
                + "+" + "-".repeat(CATEGORY_WIDTH + 2)
                + "+" + "-".repeat(PRICE_WIDTH + 2) + "+");
    }

    private void printMenuTitle(String title) {
        int insideWidth = 5 + (MENU_NAME_WIDTH + 2) + (CATEGORY_WIDTH + 2) + (PRICE_WIDTH + 2) + 3;
        int leftPadding = (insideWidth - title.length()) / 2;
        int rightPadding = insideWidth - title.length() - leftPadding;
        System.out.println("|" + " ".repeat(leftPadding) + title + " ".repeat(rightPadding) + "|");
    }

    private String formatCurrency(java.math.BigDecimal amount) {
        return "PHP " + currency.format(amount);
    }

    public void print(String message) { System.out.println(message); }
}
