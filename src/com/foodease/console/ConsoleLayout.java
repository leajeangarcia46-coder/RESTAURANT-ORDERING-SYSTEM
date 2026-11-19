package com.foodease.console;

import com.foodease.domain.MenuCategory;
import com.foodease.domain.MenuItem;
import com.foodease.domain.Order;
import com.foodease.domain.OrderItem;
import com.foodease.domain.OrderType;
import com.foodease.domain.Receipt;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Shared terminal formatting primitives. Screen classes provide content; this class owns layout. */
final class ConsoleLayout {
    static final int FRAME_WIDTH = 60;
    static final int FRAME_CONTENT_WIDTH = FRAME_WIDTH - 4;
    private static final int MENU_NAME_WIDTH = 34;
    private static final int MENU_CATEGORY_WIDTH = 16;
    private static final int MENU_PRICE_WIDTH = 14;
    private static final NumberFormat CURRENCY = NumberFormat.getNumberInstance(Locale.US);

    static {
        CURRENCY.setMinimumFractionDigits(2);
        CURRENCY.setMaximumFractionDigits(2);
    }

    private ConsoleLayout() { }

    static void inputPrompt(String prompt) {
        String label = truncate(prompt.trim(), FRAME_CONTENT_WIDTH);
        border();
        System.out.println(ConsoleTheme.rose("| ")
                + ConsoleTheme.sage(String.format("%-" + FRAME_CONTENT_WIDTH + "s", label))
                + ConsoleTheme.rose(" |"));
        border();
        CenteredConsole.printPromptMarker();
    }

    static void border() {
        System.out.println(ConsoleTheme.rose("+" + "-".repeat(FRAME_WIDTH - 2) + "+"));
    }

    static void separator() {
        System.out.println(ConsoleTheme.rose("|" + "-".repeat(FRAME_WIDTH - 2) + "|"));
    }

    static void centeredFrameLine(String text) {
        int width = FRAME_WIDTH - 2;
        String visible = truncate(text, width);
        int left = (width - visible.length()) / 2;
        System.out.println(ConsoleTheme.rose("|" + " ".repeat(left) + visible
                + " ".repeat(width - visible.length() - left) + "|"));
    }

    static void centeredTitle(String text) {
        String visible = truncate(text, FRAME_WIDTH);
        int left = (FRAME_WIDTH - visible.length()) / 2;
        System.out.println(ConsoleTheme.sage(" ".repeat(left) + visible
                + " ".repeat(FRAME_WIDTH - visible.length() - left)));
    }

    static void leftFrameLine(String text) {
        System.out.println(ConsoleTheme.rose("| " + String.format("%-" + FRAME_CONTENT_WIDTH + "s",
                truncate(text, FRAME_CONTENT_WIDTH)) + " |"));
    }

    static void wrappedFrameLines(String text) {
        for (String line : wrap(text, FRAME_CONTENT_WIDTH)) leftFrameLine(line);
    }

    static void message(String text) {
        System.out.println();
        border();
        leftFrameLine(text);
        border();
    }

    static List<String> wrap(String text, int width) {
        List<String> lines = new ArrayList<>();
        String remaining = text == null ? "" : text;
        do {
            if (remaining.length() <= width) {
                lines.add(remaining);
                break;
            }
            int breakAt = remaining.lastIndexOf(' ', width);
            if (breakAt <= 0) breakAt = width;
            lines.add(remaining.substring(0, breakAt));
            remaining = "  " + remaining.substring(breakAt).trim();
        } while (true);
        return lines;
    }

    static void billRow(String label, BigDecimal amount) {
        String value = money(amount);
        int labelWidth = FRAME_CONTENT_WIDTH - value.length();
        leftFrameLine(String.format("%-" + labelWidth + "s%s", label, value));
    }

    static String money(BigDecimal amount) {
        return "PHP " + CURRENCY.format(amount);
    }

    static void menu(List<MenuItem> menu) {
        menuBorder();
        menuTitle("RESTAURANT MENU");
        menuBorder();
        System.out.printf("| %-3s | %-" + MENU_NAME_WIDTH + "s | %-" + MENU_CATEGORY_WIDTH
                        + "s | %" + MENU_PRICE_WIDTH + "s |%n",
                "ID", "Menu item", "Category", "Price");
        menuBorder();
        for (MenuCategory category : MenuCategory.values()) {
            List<MenuItem> items = menu.stream().filter(item -> item.category() == category).toList();
            if (items.isEmpty()) continue;
            menuCategory(category.displayName());
            for (MenuItem item : items) {
                System.out.printf("| %-3s | %-" + MENU_NAME_WIDTH + "s | %-" + MENU_CATEGORY_WIDTH
                                + "s | %" + MENU_PRICE_WIDTH + "s |%n",
                        item.id(), item.name(), category.displayName(), money(item.price()));
            }
            menuBorder();
        }
    }

    static int menuScreenHeight(List<MenuItem> menu) {
        int categoryCount = 0;
        for (MenuCategory category : MenuCategory.values()) {
            if (menu.stream().anyMatch(item -> item.category() == category)) categoryCount++;
        }
        // Menu grid, add-mode instructions, and the first item-number prompt.
        return 14 + menu.size() + (2 * categoryCount);
    }

    static int setupScreenHeight(Order order) {
        // Setup details, order options, and the selection prompt.
        return order.type() == OrderType.DINE_IN ? 18 : 17;
    }

    static int cartScreenHeight(Order order, int trailingRows) {
        int lines = order.items().isEmpty() ? 15 : 13 + (2 * order.items().size());
        if (order.type() == OrderType.DINE_IN) lines++;
        int number = 1;
        for (OrderItem item : order.items()) {
            lines += wrap(number++ + ") " + item.menuItem().name(), FRAME_CONTENT_WIDTH).size() - 1;
            if (!item.note().isBlank()) {
                lines += wrap("   Request: " + item.note(), FRAME_CONTENT_WIDTH).size();
            }
        }
        return lines + trailingRows;
    }

    static int receiptScreenHeight(Receipt receipt, Order order) {
        int lines = 26; // Includes the paper-receipt prompt following the receipt.
        if (receipt.orderType() == OrderType.DINE_IN) lines++;
        lines += wrap("Receipt no.: " + receipt.receiptNumber(), FRAME_CONTENT_WIDTH).size() - 1;
        lines += wrap("Customer: " + receipt.customer().name(), FRAME_CONTENT_WIDTH).size() - 1;
        lines += wrap("Issued: " + receipt.issuedAt().toString().replace('T', ' '), FRAME_CONTENT_WIDTH).size() - 1;
        int number = 1;
        for (OrderItem item : order.items()) {
            lines += wrap(number++ + ") " + item.menuItem().name(), FRAME_CONTENT_WIDTH).size() + 2;
            if (!item.note().isBlank()) {
                lines += wrap("   Special request: " + item.note(), FRAME_CONTENT_WIDTH).size();
            }
        }
        if (receipt.payment().method().equals("cash")) lines += 2;
        return lines;
    }

    private static void menuBorder() {
        System.out.println(ConsoleTheme.rose("+-----+" + "-".repeat(MENU_NAME_WIDTH + 2)
                + "+" + "-".repeat(MENU_CATEGORY_WIDTH + 2)
                + "+" + "-".repeat(MENU_PRICE_WIDTH + 2) + "+"));
    }

    private static void menuTitle(String title) {
        int interior = 5 + MENU_NAME_WIDTH + 2 + MENU_CATEGORY_WIDTH + 2 + MENU_PRICE_WIDTH + 2 + 3;
        int left = (interior - title.length()) / 2;
        System.out.println(ConsoleTheme.rose("|" + " ".repeat(left) + title
                + " ".repeat(interior - title.length() - left) + "|"));
    }

    private static void menuCategory(String category) {
        int interior = 5 + MENU_NAME_WIDTH + 2 + MENU_CATEGORY_WIDTH + 2 + MENU_PRICE_WIDTH + 2 + 3;
        String label = " " + category;
        System.out.println(ConsoleTheme.sage("|" + label
                + " ".repeat(Math.max(0, interior - label.length())) + "|"));
    }

    private static String truncate(String text, int width) {
        return text.length() <= width ? text : text.substring(0, Math.max(0, width - 3)) + "...";
    }
}
