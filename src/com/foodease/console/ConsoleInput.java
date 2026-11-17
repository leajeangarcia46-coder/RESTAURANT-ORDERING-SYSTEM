package com.foodease.console;

import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner = new Scanner(System.in);
    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public String readFramedLine(String prompt) {
        System.out.print("| " + prompt);
        String value = scanner.nextLine().trim();
        int contentLength = 2 + prompt.length() + value.length();
        int padding = Math.max(1, ConsoleOutput.CONSOLE_WIDTH - contentLength - 1);
        System.out.println(" ".repeat(padding) + "|");
        return value;
    }

    public int readInt(String prompt) {
        while (true) {
            try { return Integer.parseInt(readLine(prompt)); }
            catch (NumberFormatException ex) { System.out.println("Enter a whole number."); }
        }
    }

    public int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) return value;
            System.out.println("Enter a number greater than zero.");
        }
    }

    public int readFramedInt(String prompt) {
        while (true) {
            try { return Integer.parseInt(readFramedLine(prompt)); }
            catch (NumberFormatException ex) { printFramedMessage("Enter a whole number."); }
        }
    }

    public int readFramedPositiveInt(String prompt) {
        while (true) {
            int value = readFramedInt(prompt);
            if (value > 0) return value;
            printFramedMessage("Enter a number greater than zero.");
        }
    }

    private void printFramedMessage(String message) {
        int padding = Math.max(1, ConsoleOutput.CONSOLE_WIDTH - message.length() - 3);
        System.out.println("| " + message + " ".repeat(padding) + "|");
    }
}
