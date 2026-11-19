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
        CenteredConsole.printPromptMarker();
    }

    public void showWelcome() {
        CenteredConsole.beginWelcome(19);
        drawWelcome();
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
        CenteredConsole.beginScreen(order.type() == OrderType.DINE_IN ? 18 : 17);
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
        CenteredConsole.beginScreen(14 + menu.size() + (2 * categoryCount));
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
        int receiptLines = 26; // Includes the paper-receipt prompt that follows this panel.
        if (receipt.orderType() == OrderType.DINE_IN) receiptLines++;
        receiptLines += wrappedLineCount("Receipt no.: " + receipt.receiptNumber()) - 1;
        receiptLines += wrappedLineCount("Customer: " + receipt.customer().name()) - 1;
        receiptLines += wrappedLineCount("Issued: " + receipt.issuedAt().toString().replace('T', ' ')) - 1;
        int itemNumber = 1;
        for (OrderItem item : order.items()) {
            receiptLines += wrappedLineCount(itemNumber++ + ") " + item.menuItem().name()) + 2;
            if (!item.note().isBlank()) receiptLines += wrappedLineCount("   Special request: " + item.note());
        }
        if (receipt.payment().method().equals("cash")) receiptLines += 2;
        CenteredConsole.beginScreen(receiptLines);
        System.out.println();
        printFrameBorder();
        printFrameLine("FOODEASE RESTAURANT");
        printFrameLine("CUSTOMER RECEIPT");
        printFrameBorder();
        printWrappedFrameLines("Receipt no.: " + receipt.receiptNumber());
        printWrappedFrameLines("Customer: " + receipt.customer().name());
        printLeftFrameLine("Order type: " + orderTypeLabel(receipt.orderType()));
        if (receipt.orderType() == OrderType.DINE_IN) {
            printLeftFrameLine("Table: " + order.tableNumber());
        }
        printWrappedFrameLines("Issued: " + receipt.issuedAt().toString().replace('T', ' '));
        printFrameSeparator();
        printFrameLine("ITEMIZED ORDER");
        printFrameSeparator();
        itemNumber = 1;
        for (OrderItem item : order.items()) {
            printWrappedFrameLines(itemNumber++ + ") " + item.menuItem().name());
            printLeftFrameLine("   Quantity: " + item.quantity() + " x " + formatCurrency(item.unitPrice()));
            printLeftFrameLine("   Line total: " + formatCurrency(item.subtotal()));
            if (!item.note().isBlank()) printWrappedFrameLines("   Special request: " + item.note());
        }
        printFrameSeparator();
        printFrameLine("PAYMENT SUMMARY");
        printFrameSeparator();
        printBillRow("Subtotal", receipt.subtotal());
        printBillRow(receipt.taxIncludedInPrices() ? "Tax included in prices" : "Tax", receipt.tax());
        printBillRow("Service charge", receipt.serviceCharge());
        printBillRow("TOTAL DUE", receipt.total());
        printLeftFrameLine("Payment method: " + capitalize(receipt.payment().method()));
        if (receipt.payment().method().equals("cash")) {
            printBillRow("Cash received", receipt.payment().amountTendered());
            printBillRow("Change", receipt.payment().changeDue());
        }
        printFrameBorder();
    }

    public void showCart(Order order, Bill bill) {
        showCart(order, bill, 11);
    }

    /** The trailing row count includes the next prompt or action panel on this screen. */
    public void showCart(Order order, Bill bill, int trailingRows) {
        int cartLines = order.items().isEmpty() ? 15 : 13 + (2 * order.items().size());
        if (order.type() == OrderType.DINE_IN) cartLines++;
        cartLines += (int) order.items().stream().filter(item -> !item.note().isBlank()).count();
        CenteredConsole.beginScreen(cartLines + trailingRows);
        System.out.println();
        printFrameBorder();
        printFrameLine("CURRENT ORDER");
        printFrameBorder();
        printLeftFrameLine("Order type: " + orderTypeLabel(order.type()));
        if (order.type() == OrderType.DINE_IN) {
            printLeftFrameLine("Table: " + order.tableNumber());
        }
        printFrameSeparator();
        printFrameLine("ORDER ITEMS");
        if (order.items().isEmpty()) {
            printLeftFrameLine("No items have been added yet.");
            printFrameSeparator();
            showBill(bill);
            printFrameBorder();
            return;
        }
        int number = 1;
        for (OrderItem item : order.items()) {
            printWrappedFrameLines(number++ + ") " + item.menuItem().name());
            printLeftFrameLine("   Qty: " + item.quantity() + " x " + formatCurrency(item.unitPrice())
                    + "  |  Line total: " + formatCurrency(item.subtotal()));
            if (!item.note().isBlank()) printWrappedFrameLines("   Request: " + item.note());
        }
        printFrameSeparator();
        showBill(bill);
        printFrameBorder();
    }

    public void showBill(Bill bill) {
        printBillRow("Items subtotal", bill.subtotal());
        printBillRow(bill.taxIncludedInPrices() ? "Tax included in prices" : "Tax", bill.tax());
        printBillRow("Service charge", bill.serviceCharge());
        printBillRow("TOTAL DUE", bill.total());
    }

    private void printBillRow(String label, java.math.BigDecimal amount) {
        int innerWidth = CONSOLE_WIDTH - 4;
        String value = formatCurrency(amount);
        String row = String.format("%-" + (innerWidth - value.length()) + "s%s", label, value);
        printLeftFrameLine(row);
    }

    private void printFrameSeparator() {
        System.out.println(ConsoleTheme.rose("|" + "-".repeat(CONSOLE_WIDTH - 2) + "|"));
    }

    private void printWrappedFrameLines(String text) {
        int width = CONSOLE_WIDTH - 4;
        String remaining = text;
        while (remaining.length() > width) {
            int breakAt = remaining.lastIndexOf(' ', width);
            if (breakAt <= 0) breakAt = width;
            printLeftFrameLine(remaining.substring(0, breakAt));
            remaining = "  " + remaining.substring(breakAt).trim();
        }
        printLeftFrameLine(remaining);
    }

    private int wrappedLineCount(String text) {
        int width = CONSOLE_WIDTH - 4;
        int count = 0;
        String remaining = text;
        do {
            count++;
            if (remaining.length() <= width) break;
            int breakAt = remaining.lastIndexOf(' ', width);
            if (breakAt <= 0) breakAt = width;
            remaining = "  " + remaining.substring(breakAt).trim();
        } while (true);
        return count;
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
        int visibleLength = Math.min(text.length(), CONSOLE_WIDTH);
        String visibleText = text.substring(0, visibleLength);
        int leftPadding = (CONSOLE_WIDTH - visibleLength) / 2;
        int rightPadding = CONSOLE_WIDTH - visibleLength - leftPadding;
        System.out.println(ConsoleTheme.sage(" ".repeat(leftPadding) + visibleText
                + " ".repeat(rightPadding)));
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
