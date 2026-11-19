package com.foodease.console;

import com.foodease.domain.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConsoleOutput {
    public void showWelcome() {
        CenteredConsole.beginWelcome(19);
        drawWelcome();
    }

    private void drawWelcome() {
        ConsoleLayout.centeredFrameLine("============================================================");
        ConsoleLayout.centeredTitle("FOODEASE RESTAURANT");
        ConsoleLayout.centeredTitle("ORDERING & CHECKOUT");
        ConsoleLayout.centeredFrameLine("============================================================");
        ConsoleLayout.border();
        ConsoleLayout.centeredFrameLine("Welcome! Start a new dine-in or takeout order.");
        ConsoleLayout.border();
    }

    public void showOrderSetup(Order order) {
        CenteredConsole.beginScreen(ConsoleLayout.setupScreenHeight(order));
        ConsoleLayout.border();
        ConsoleLayout.centeredFrameLine("ORDER DETAILS");
        ConsoleLayout.border();
        ConsoleLayout.centeredFrameLine("Customer: " + order.customer().name());
        ConsoleLayout.centeredFrameLine("Order type: " + order.type().displayName());
        if (order.type() == OrderType.DINE_IN) {
            ConsoleLayout.centeredFrameLine("Table: " + order.tableNumber());
        }
        ConsoleLayout.border();
    }

    public void showOrderActions() {
        System.out.println();
        ConsoleLayout.border();
        ConsoleLayout.centeredFrameLine("ORDER OPTIONS");
        ConsoleLayout.border();
        ConsoleLayout.centeredFrameLine("1) Add item     2) View cart      3) Remove item");
        ConsoleLayout.centeredFrameLine("4) Change qty   5) Checkout      6) Cancel");
        ConsoleLayout.border();
    }

    public void showAddItemsInstructions() {
        ConsoleLayout.border();
        ConsoleLayout.centeredFrameLine("ADD ITEMS TO YOUR ORDER");
        ConsoleLayout.border();
        ConsoleLayout.leftFrameLine("Add as many items as needed. Enter B when finished.");
        ConsoleLayout.border();
    }

    public void showAddedItem(MenuItem item, int quantity, BigDecimal lineTotal, Bill bill) {
        System.out.println();
        ConsoleLayout.border();
        ConsoleLayout.centeredFrameLine("ITEM ADDED");
        ConsoleLayout.border();
        ConsoleLayout.leftFrameLine("Added " + quantity + " x " + item.name() + " to the order.");
        ConsoleLayout.leftFrameLine("Item total: " + ConsoleLayout.money(lineTotal));
        ConsoleLayout.leftFrameLine("Order total: " + ConsoleLayout.money(bill.total()));
        ConsoleLayout.border();
    }

    public void showAddItemsFinished() {
        System.out.println();
        ConsoleLayout.border();
        ConsoleLayout.centeredFrameLine("Finished adding items");
        ConsoleLayout.border();
    }

    public void showAddItemMessage(String text) {
        System.out.println();
        ConsoleLayout.border();
        ConsoleLayout.centeredFrameLine(text);
        ConsoleLayout.border();
    }

    public void showMenu(Iterable<MenuItem> items) {
        List<MenuItem> menu = new ArrayList<>();
        items.forEach(menu::add);
        CenteredConsole.beginScreen(ConsoleLayout.menuScreenHeight(menu));
        ConsoleLayout.menu(menu);
    }

    public void showReceipt(Receipt receipt, Order order) {
        CenteredConsole.beginScreen(ConsoleLayout.receiptScreenHeight(receipt, order));
        System.out.println();
        ConsoleLayout.border();
        ConsoleLayout.centeredFrameLine("FOODEASE RESTAURANT");
        ConsoleLayout.centeredFrameLine("CUSTOMER RECEIPT");
        ConsoleLayout.border();
        ConsoleLayout.wrappedFrameLines("Receipt no.: " + receipt.receiptNumber());
        ConsoleLayout.wrappedFrameLines("Customer: " + receipt.customer().name());
        ConsoleLayout.leftFrameLine("Order type: " + receipt.orderType().displayName());
        if (receipt.orderType() == OrderType.DINE_IN) {
            ConsoleLayout.leftFrameLine("Table: " + order.tableNumber());
        }
        ConsoleLayout.wrappedFrameLines("Issued: " + receipt.issuedAt().toString().replace('T', ' '));
        ConsoleLayout.separator();
        ConsoleLayout.centeredFrameLine("ITEMIZED ORDER");
        ConsoleLayout.separator();
        int itemNumber = 1;
        for (OrderItem item : order.items()) {
            ConsoleLayout.wrappedFrameLines(itemNumber++ + ") " + item.menuItem().name());
            ConsoleLayout.leftFrameLine("   Quantity: " + item.quantity() + " x " + ConsoleLayout.money(item.unitPrice()));
            ConsoleLayout.leftFrameLine("   Line total: " + ConsoleLayout.money(item.subtotal()));
            if (!item.note().isBlank()) ConsoleLayout.wrappedFrameLines("   Special request: " + item.note());
        }
        ConsoleLayout.separator();
        ConsoleLayout.centeredFrameLine("PAYMENT SUMMARY");
        ConsoleLayout.separator();
        ConsoleLayout.billRow("Subtotal", receipt.subtotal());
        ConsoleLayout.billRow(receipt.taxIncludedInPrices() ? "Tax included in prices" : "Tax", receipt.tax());
        ConsoleLayout.billRow("Service charge", receipt.serviceCharge());
        ConsoleLayout.billRow("TOTAL DUE", receipt.total());
        ConsoleLayout.leftFrameLine("Payment method: " + receipt.payment().method().toUpperCase(Locale.ROOT));
        if (receipt.payment().method().equals("cash")) {
            ConsoleLayout.billRow("Cash received", receipt.payment().amountTendered());
            ConsoleLayout.billRow("Change", receipt.payment().changeDue());
        }
        ConsoleLayout.border();
    }

    /** The trailing row count includes the next prompt or action panel on this screen. */
    public void showCart(Order order, Bill bill, int trailingRows) {
        CenteredConsole.beginScreen(ConsoleLayout.cartScreenHeight(order, trailingRows));
        System.out.println();
        ConsoleLayout.border();
        ConsoleLayout.centeredFrameLine("CURRENT ORDER");
        ConsoleLayout.border();
        ConsoleLayout.leftFrameLine("Order type: " + order.type().displayName());
        if (order.type() == OrderType.DINE_IN) {
            ConsoleLayout.leftFrameLine("Table: " + order.tableNumber());
        }
        ConsoleLayout.separator();
        ConsoleLayout.centeredFrameLine("ORDER ITEMS");
        if (order.items().isEmpty()) {
            ConsoleLayout.leftFrameLine("No items have been added yet.");
            ConsoleLayout.separator();
            showBill(bill);
            ConsoleLayout.border();
            return;
        }
        int number = 1;
        for (OrderItem item : order.items()) {
            ConsoleLayout.wrappedFrameLines(number++ + ") " + item.menuItem().name());
            ConsoleLayout.leftFrameLine("   Qty: " + item.quantity() + " x " + ConsoleLayout.money(item.unitPrice())
                    + "  |  Line total: " + ConsoleLayout.money(item.subtotal()));
            if (!item.note().isBlank()) ConsoleLayout.wrappedFrameLines("   Request: " + item.note());
        }
        ConsoleLayout.separator();
        showBill(bill);
        ConsoleLayout.border();
    }

    public void showBill(Bill bill) {
        ConsoleLayout.billRow("Items subtotal", bill.subtotal());
        ConsoleLayout.billRow(bill.taxIncludedInPrices() ? "Tax included in prices" : "Tax", bill.tax());
        ConsoleLayout.billRow("Service charge", bill.serviceCharge());
        ConsoleLayout.billRow("TOTAL DUE", bill.total());
    }

    public void print(String message) {
        ConsoleLayout.message(message);
    }
}
