package com.foodease.console;

import java.util.Objects;
import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput() {
        this(new Scanner(System.in));
    }

    public ConsoleInput(Scanner scanner) {
        this.scanner = Objects.requireNonNull(scanner);
    }

    public String readCustomerName() {
        return readLine("Customer name: ");
    }

    public String readLine(String prompt) {
        ConsoleLayout.inputPrompt(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            try { return Integer.parseInt(readLine(prompt)); }
            catch (NumberFormatException ex) { ConsoleLayout.message("Enter a whole number."); }
        }
    }

    public int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) return value;
            ConsoleLayout.message("Enter a number greater than zero.");
        }
    }

}
