package com.foodease.console;

import com.foodease.domain.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
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

    /** Shows a labeled prompt in the same framed style as menu and status panels. */
    public static void showInputPrompt(String prompt) {
        int innerWidth = CONSOLE_WIDTH - 4;
        String label = prompt.trim();
        if (label.length() > innerWidth) label = label.substring(0, innerWidth - 3) + "...";
        System.out.println(ConsoleTheme.rose("+" + "-".repeat(CONSOLE_WIDTH - 2) + "+"));
        System.out.println(ConsoleTheme.rose("| ") + ConsoleTheme.sage(String.format("%-" + innerWidth + "s", label))
                + ConsoleTheme.rose(" |"));
        System.out.println(ConsoleTheme.rose("+" + "-".repeat(CONSOLE_WIDTH - 2) + "+"));
        System.out.print(ConsoleTheme.sage("> "));
        System.out.flush();
    }

    public void showWelcome() {
        CenteredConsole.beginWelcome(8);
        drawWelcome();
        CenteredConsole.finishWelcome();
    }

    private void drawWelcome() {
        System.out.println(ConsoleTheme.rose("============================================================"));
        printCentered("FOODEASE RESTAURANT");
        printCentered("ORDERING & CHECKOUT");
        System.out.println(ConsoleTheme.rose("============================================================"));
        printFrameBorder();
        printFrameLine("Welcome! Start a new dine-in or takeout order.");
        printFrameBorder();
    }

    public void showOrderSetup(Order order) {
        CenteredConsole.beginScreen(order.type() == OrderType.DINE_IN ? 13 : 12);
        printFrameBorder();
        printFrameLine("ORDER DETAILS");
        printFrameBorder();
        printFrameLine("Customer: " + order.customer().name());
        printFrameLine("Order type: " + orderTypeLabel(order.type()));
        if (order.type() == OrderType.DINE_IN) {
            printFrameLine("Table: " + order.tableNumber());
        }
        printFrameBorder();
    }

    public void showOrderActions() {
        System.out.println();
        printFrameBorder();
        printFrameLine("ORDER OPTIONS");
        printFrameBorder();
        printFrameLine("1) Add item     2) View cart      3) Remove item");
        printFrameLine("4) Change qty   5) Checkout      6) Cancel");
        printFrameBorder();
    }

    public void showAddItemsInstructions() {
        printFrameBorder();
        printFrameLine("ADD ITEMS TO YOUR ORDER");
        printFrameBorder();
        printLeftFrameLine("Add as many items as needed. Enter B when finished.");
        printFrameBorder();
    }

    public void showAddedItem(MenuItem item, int quantity, java.math.BigDecimal lineTotal,
                              Bill bill) {
        System.out.println();
        printFrameBorder();
        printFrameLine("ITEM ADDED");
        printFrameBorder();
        printLeftFrameLine("Added " + quantity + " x " + item.name() + " to the order.");
        printLeftFrameLine("Item total: " + formatCurrency(lineTotal));
        printLeftFrameLine("Order total: " + formatCurrency(bill.total()));
        printFrameBorder();
    }

    public void showAddItemsFinished() {
        System.out.println();
        printFrameBorder();
        printFrameLine("Finished adding items");
        printFrameBorder();
    }

    public void showAddItemMessage(String text) {
        System.out.println();
        printFrameBorder();
        printFrameLine(text);
        printFrameBorder();
    }

    private void printLeftFrameLine(String text) {
        int innerWidth = CONSOLE_WIDTH - 4;
        String visibleText = text.length() > innerWidth ? text.substring(0, innerWidth) : text;
        System.out.printf("| %-" + innerWidth + "s |%n", visibleText);
    }

    public void showMenu(Iterable<MenuItem> items) {
        List<MenuItem> menu = new ArrayList<>();
        items.forEach(menu::add);
        int categoryCount = 0;
        for (MenuCategory category : MenuCategory.values()) {
            if (menu.stream().anyMatch(item -> item.category() == category)) categoryCount++;
        }
        CenteredConsole.beginScreen(11 + menu.size() + (2 * categoryCount));
        printMenuBorder();
        printMenuTitle("RESTAURANT MENU");
        printMenuBorder();
        System.out.printf("| %-3s | %-" + MENU_NAME_WIDTH + "s | %-" + CATEGORY_WIDTH + "s | %" + PRICE_WIDTH + "s |%n",
                "ID", "Menu item", "Category", "Price");
        printMenuBorder();
        for (MenuCategory category : MenuCategory.values()) {
            List<MenuItem> categorized = menu.stream().filter(item -> item.category() == category).toList();
            if (categorized.isEmpty()) continue;
            printMenuCategory(categoryLabel(category));
            for (MenuItem item : categorized) {
                System.out.printf("| %-3s | %-" + MENU_NAME_WIDTH + "s | %-" + CATEGORY_WIDTH + "s | %" + PRICE_WIDTH + "s |%n",
                        item.id(), item.name(), categoryLabel(item.category()), formatCurrency(item.price()));
            }
            printMenuBorder();
        }
    }

    public void showReceipt(Receipt receipt, Order order) {
        int receiptLines = 19 + order.items().size();
        if (receipt.orderType() == OrderType.DINE_IN) receiptLines++;
        receiptLines += (int) order.items().stream().filter(item -> !item.note().isBlank()).count();
        if (receipt.payment().method().equals("cash")) receiptLines += 2;
        CenteredConsole.beginScreen(receiptLines);
        System.out.println("\n============================================================");
        System.out.println("                  FOODEASE RESTAURANT");
        System.out.println("                       RECEIPT");
        System.out.println("Order No.: " + receipt.receiptNumber());
        System.out.println("Customer: " + receipt.customer().name());
        System.out.println("Order type: " + orderTypeLabel(receipt.orderType()));
        if (receipt.orderType() == OrderType.DINE_IN) {
            System.out.println("Table: " + order.tableNumber());
        }
        System.out.println("Issued: " + receipt.issuedAt().toString().replace('T', ' '));
        System.out.println("------------------------------------------------------------");
        for (OrderItem item : order.items())
            System.out.printf("%-30s %3d x %10s = %10s%n", item.menuItem().name(), item.quantity(),
                    formatCurrency(item.unitPrice()), formatCurrency(item.subtotal()));
        for (OrderItem item : order.items()) {
            if (!item.note().isBlank()) System.out.println("  Note: " + item.note());
        }
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-40s %10s%n", "Subtotal", formatCurrency(receipt.subtotal()));
        System.out.printf("%-40s %10s%n", receipt.taxIncludedInPrices() ? "Tax included in prices" : "Tax",
                formatCurrency(receipt.tax()));
        System.out.printf("%-40s %10s%n", "Service charge", formatCurrency(receipt.serviceCharge()));
        System.out.printf("%-40s %10s%n", "TOTAL DUE", formatCurrency(receipt.total()));
        System.out.println("Payment: " + capitalize(receipt.payment().method()));
        if (receipt.payment().method().equals("cash")) {
            System.out.printf("%-40s %10s%n", "Cash received", formatCurrency(receipt.payment().amountTendered()));
            System.out.printf("%-40s %10s%n", "Change", formatCurrency(receipt.payment().changeDue()));
        }
        System.out.println("============================================================");
    }

    public void showCart(Order order, Bill bill) {
        int cartLines = order.items().isEmpty() ? 8 : 13 + order.items().size();
        if (order.type() == OrderType.DINE_IN) cartLines++;
        cartLines += (int) order.items().stream().filter(item -> !item.note().isBlank()).count();
        CenteredConsole.beginScreen(cartLines);
        System.out.println();
        printFrameBorder();
        printFrameLine("CURRENT ORDER");
        printFrameBorder();
        System.out.println("Type: " + orderTypeLabel(order.type()));
        if (order.type() == OrderType.DINE_IN) {
            System.out.println("Table: " + order.tableNumber());
        }
        if (order.items().isEmpty()) {
            System.out.println("No items have been added yet.");
            printFrameBorder();
            return;
        }
        System.out.println("------------------------------------------------------------");
        int number = 1;
        for (OrderItem item : order.items()) {
            System.out.printf("%d. %-28s %3d x %10s = %10s%n", number++, item.menuItem().name(),
                    item.quantity(), formatCurrency(item.unitPrice()), formatCurrency(item.subtotal()));
            if (!item.note().isBlank()) System.out.println("   Note: " + item.note());
        }
        System.out.println("------------------------------------------------------------");
        showBill(bill);
        printFrameBorder();
    }

    public void showBill(Bill bill) {
        System.out.printf("%-40s %10s%n", "Items subtotal", formatCurrency(bill.subtotal()));
        System.out.printf("%-40s %10s%n", bill.taxIncludedInPrices() ? "Tax included in prices" : "Tax",
                formatCurrency(bill.tax()));
        System.out.printf("%-40s %10s%n", "Service charge", formatCurrency(bill.serviceCharge()));
        System.out.printf("%-40s %10s%n", "TOTAL DUE", formatCurrency(bill.total()));
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
        System.out.println(ConsoleTheme.sage(" ".repeat(leftPadding) + text));
    }

    private void printFrameBorder() {
        System.out.println(ConsoleTheme.rose("+" + "-".repeat(CONSOLE_WIDTH - 2) + "+"));
    }

    private void printFrameLine(String text) {
        int insideWidth = CONSOLE_WIDTH - 2;
        int visibleLength = Math.min(text.length(), insideWidth);
        String visibleText = text.substring(0, visibleLength);
        int leftPadding = (insideWidth - visibleLength) / 2;
        int rightPadding = insideWidth - visibleLength - leftPadding;
        System.out.println(ConsoleTheme.rose("|" + " ".repeat(leftPadding) + visibleText
                + " ".repeat(rightPadding) + "|"));
    }

    private void printMenuBorder() {
        System.out.println(ConsoleTheme.rose("+-----+" + "-".repeat(MENU_NAME_WIDTH + 2)
                + "+" + "-".repeat(CATEGORY_WIDTH + 2)
                + "+" + "-".repeat(PRICE_WIDTH + 2) + "+"));
    }

    private void printMenuTitle(String title) {
        int insideWidth = 5 + (MENU_NAME_WIDTH + 2) + (CATEGORY_WIDTH + 2) + (PRICE_WIDTH + 2) + 3;
        int leftPadding = (insideWidth - title.length()) / 2;
        int rightPadding = insideWidth - title.length() - leftPadding;
        System.out.println(ConsoleTheme.rose("|" + " ".repeat(leftPadding) + title + " ".repeat(rightPadding) + "|"));
    }

    private void printMenuCategory(String category) {
        int insideWidth = 5 + (MENU_NAME_WIDTH + 2) + (CATEGORY_WIDTH + 2) + (PRICE_WIDTH + 2) + 3;
        int contentWidth = insideWidth - 2;
        String label = " " + category;
        System.out.println(ConsoleTheme.sage("|" + label + " ".repeat(Math.max(0, contentWidth - label.length())) + "|"));
    }

    private String formatCurrency(java.math.BigDecimal amount) {
        return "PHP " + currency.format(amount);
    }

    public void print(String message) {
        System.out.println();
        printFrameBorder();
        printLeftFrameLine(message);
        printFrameBorder();
    }
}
