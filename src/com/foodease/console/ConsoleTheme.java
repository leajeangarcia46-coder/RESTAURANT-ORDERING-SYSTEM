package com.foodease.console;

/** ANSI colors for terminals that support 24-bit color output. */
final class ConsoleTheme {
    private static final String RESET = "\u001B[0m";
    private static final String ROSE = "\u001B[38;2;206;152;172m"; // #CE98AC
    private static final String SAGE = "\u001B[38;2;171;194;157m"; // #ABC29D
    private static final boolean ENABLED = System.console() != null
            && System.getenv("NO_COLOR") == null;

    private ConsoleTheme() { }

    static String rose(String text) { return colorize(ROSE, text); }
    static String sage(String text) { return colorize(SAGE, text); }

    private static String colorize(String color, String text) {
        return ENABLED ? color + text + RESET : text;
    }
}
