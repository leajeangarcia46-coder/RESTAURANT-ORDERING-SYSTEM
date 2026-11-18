package com.foodease.console;

/** ANSI colors for terminals that support 24-bit color output. */
final class ConsoleTheme {
    private static final String RESET = "\u001B[0m";
    private static final boolean ENABLED = System.console() != null
            && System.getenv("NO_COLOR") == null;

    private ConsoleTheme() { }

    static String rose(String text) { return colorize(206, 152, 172, text); }
    static String sage(String text) { return colorize(171, 194, 157, text); }

    private static String colorize(int red, int green, int blue, String text) {
        if (!ENABLED) return text;
        return "\u001B[38;2;" + red + ";" + green + ";" + blue + "m" + text + RESET;
    }
}
