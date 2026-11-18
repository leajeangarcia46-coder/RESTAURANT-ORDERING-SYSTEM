package com.foodease.console;

/** ANSI colors for terminals that support 24-bit color output. */
final class ConsoleTheme {
    private static final String RESET = "\u001B[0m";
    private static volatile double fade = 1.0;
    private static final boolean ENABLED = System.console() != null
            && System.getenv("NO_COLOR") == null;

    private ConsoleTheme() { }

    static void setFade(double progress) { fade = Math.max(0.0, Math.min(1.0, progress)); }

    static String rose(String text) { return colorize(206, 152, 172, text); }
    static String sage(String text) { return colorize(171, 194, 157, text); }

    private static String colorize(int red, int green, int blue, String text) {
        if (!ENABLED) return text;
        int r = (int) Math.round(red * fade);
        int g = (int) Math.round(green * fade);
        int b = (int) Math.round(blue * fade);
        return "\u001B[38;2;" + r + ";" + g + ";" + b + "m" + text + RESET;
    }
}
