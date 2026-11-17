package com.foodease.console;

import com.foodease.application.OrderService;
import com.foodease.domain.*;

public class ConsoleApp {
    private final ConsoleInput input;
    private final ConsoleOutput output;
    private final OrderService orderService;

    public ConsoleApp(ConsoleInput input, ConsoleOutput output, OrderService orderService) {
        this.input = input;
        this.output = output;
        this.orderService = orderService;
    }

    public void run() {
        output.print("Welcome to FoodEase!");
        String name;
        do {
            name = input.readLine("Customer name: ");
            if (name.isBlank()) output.print("Please enter a customer name.");
        } while (name.isBlank());

        int typeChoice;
        while (true) {
            typeChoice = input.readInt("Order type (1 dine-in, 2 takeout): ");
            if (typeChoice == 1 || typeChoice == 2) break;
            output.print("Choose 1 for dine-in or 2 for takeout.");
        }
        Order order = orderService.createOrder(name, typeChoice == 1 ? OrderType.DINE_IN : OrderType.TAKEOUT);

        while (true) {
            output.print("\nA) Add item   V) View order   R) Remove item   C) Checkout   X) Cancel");
            String action = input.readLine("Choose an action: ");
            switch (action.toUpperCase()) {
                case "A" -> addItem(order);
                case "V" -> output.showCart(order);
                case "R" -> removeItem(order);
                case "C" -> {
                    if (order.items().isEmpty()) output.print("Add at least one item before checkout.");
                    else if (checkout(order)) return;
                }
                case "X" -> {
                    output.print("Order cancelled.");
                    return;
                }
                default -> output.print("Choose A, V, R, C, or X.");
            }
        }
    }

    private void addItem(Order order) {
        output.showMenu(orderService.getMenu());
        String choice = input.readLine("Menu item number (or B to go back): ");
        if (choice.equalsIgnoreCase("B")) return;
        try {
            MenuItem item = orderService.getMenuItem(choice);
            int quantity = input.readPositiveInt("Quantity: ");
            order.addItem(item, quantity);
            output.print("Added " + quantity + " x " + item.name());
            output.showTotal(order.total());
        } catch (IllegalArgumentException ex) {
            output.print(ex.getMessage());
        }
    }

    private void removeItem(Order order) {
        output.showCart(order);
        if (order.items().isEmpty()) return;
        int itemNumber = input.readInt("Cart item number to remove (0 to go back): ");
        if (itemNumber == 0) return;
        if (itemNumber < 0) {
            output.print("Enter 0 or a positive cart item number.");
            return;
        }
        try {
            order.removeItem(itemNumber);
            output.print("Item removed.");
            output.showCart(order);
        } catch (IllegalArgumentException ex) {
            output.print(ex.getMessage());
        }
    }

    private boolean checkout(Order order) {
        while (true) {
            String method = input.readLine("Payment method (cash/card, or B to go back): ");
            if (method.equalsIgnoreCase("B")) return false;
            if (!method.equalsIgnoreCase("cash") && !method.equalsIgnoreCase("card")) {
                output.print("Choose cash or card.");
                continue;
            }
            java.math.BigDecimal tendered = order.total();
            if (method.equalsIgnoreCase("cash")) {
                while (true) {
                    String amount = input.readLine("Cash received: ");
                    try {
                        tendered = new java.math.BigDecimal(amount);
                        if (tendered.compareTo(order.total()) >= 0) break;
                        output.print("Cash received must cover the total.");
                    } catch (NumberFormatException ex) {
                        output.print("Enter a valid amount, for example 20.00.");
                    }
                }
            }
            try {
                Receipt receipt = orderService.checkout(order, method, tendered);
                output.showReceipt(receipt, order);
                output.print("Thank you for your order!");
                return true;
            } catch (IllegalArgumentException ex) {
                output.print(ex.getMessage());
            }
        }
    }
}
