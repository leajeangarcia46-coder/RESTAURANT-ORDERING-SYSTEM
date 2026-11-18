package com.foodease.console;

import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner = new Scanner(System.in);
    public String readLine(String prompt) {
        System.out.print(ConsoleTheme.sage(prompt));
        return scanner.nextLine().trim();
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

}
