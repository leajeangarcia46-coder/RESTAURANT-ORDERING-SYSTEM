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
        output.showWelcome();
        String name;
        do {
            name = input.readLine("Customer name: ");
            if (name.isBlank()) output.print("Please enter a customer name to start the order.");
        } while (name.isBlank());

        int typeChoice;
        while (true) {
            typeChoice = input.readInt("Order type (1 dine-in, 2 takeout): ");
            if (typeChoice == 1 || typeChoice == 2) break;
            output.print("Choose 1 for dine-in or 2 for takeout.");
        }
        OrderType type = typeChoice == 1 ? OrderType.DINE_IN : OrderType.TAKEOUT;
        String tableNumber = type == OrderType.DINE_IN
                ? Integer.toString(input.readPositiveInt("Table number: ")) : "";
        Order order = orderService.createOrder(name, type, tableNumber);
        order.setSpecialInstructions(input.readLine("Kitchen instructions (optional): "));
        output.showOrderSetup(order);

        while (true) {
            output.showOrderActions();
            String action = input.readLine("Select an option (1-5): ");
            switch (action.toUpperCase()) {
                case "1" -> addItem(order);
                case "2" -> output.showCart(order);
                case "3" -> removeItem(order);
                case "4" -> {
                    if (order.items().isEmpty()) output.print("Add at least one item before checkout.");
                    else if (checkout(order)) return;
                }
                case "5" -> {
                    output.print("Order cancelled.");
                    return;
                }
                default -> output.print("Enter a number from 1 to 5.");
            }
        }
    }

    private void addItem(Order order) {
        output.showMenu(orderService.getMenu());
        output.print("Add multiple items below. Enter B when you are finished.");
        while (true) {
            String choice = input.readLine("Menu item number to add (or B when finished): ");
            if (choice.equalsIgnoreCase("B")) return;
            try {
                MenuItem item = orderService.getMenuItem(choice);
                int quantity = input.readPositiveInt("Quantity: ");
                order.addItem(item, quantity);
                output.print("Added " + quantity + " x " + item.name() + " to the order.");
                output.showTotal(order.total());
            } catch (IllegalArgumentException ex) {
                output.print(ex.getMessage());
            }
        }
    }

    private void removeItem(Order order) {
        output.showCart(order);
        if (order.items().isEmpty()) return;
        int itemNumber = input.readInt("Order line to remove (0 to go back): ");
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
                        output.print("Enter a valid amount, for example 100.00.");
                    }
                }
            }
            try {
                Receipt receipt = orderService.checkout(order, method, tendered);
                output.showReceipt(receipt, order);
                output.print("Thank you for dining with FoodEase!");
                return true;
            } catch (IllegalArgumentException ex) {
                output.print(ex.getMessage());
            }
        }
    }
}
