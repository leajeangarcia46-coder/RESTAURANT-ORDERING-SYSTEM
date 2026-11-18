package com.foodease.console;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/** Keeps console output centered against the current Windows terminal dimensions. */
public final class CenteredConsole {
    private static volatile Size size = new Size(100, 40);
    private static volatile long lastSizeCheck;
    private static PrintStream terminalOutput;
    private static boolean alternateScreen;

    private CenteredConsole() { }

    public static void activate() {
        if (System.console() == null || Boolean.getBoolean("foodease.centeredConsole")) return;
        PrintStream original = System.out;
        terminalOutput = original;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (alternateScreen && terminalOutput != null) {
                terminalOutput.print("\u001B[?25h\u001B[?1049l");
                terminalOutput.flush();
            }
        }, "foodease-terminal-restore"));
        size = readTerminalSize();
        lastSizeCheck = System.nanoTime();
        OutputStream centered = new OutputStream() {
            private final ByteArrayOutputStream line = new ByteArrayOutputStream();

            @Override
            public synchronized void write(int value) {
                if (value == '\n') {
                    writeLine(original, line.toByteArray());
                    line.reset();
                    original.write('\n');
                } else {
                    line.write(value);
                }
            }

            @Override
            public synchronized void write(byte[] bytes, int offset, int length) {
                for (int index = offset; index < offset + length; index++) write(bytes[index]);
            }

            @Override
            public synchronized void flush() {
                // printf may flush between fields; keep collecting until the line ending.
                original.flush();
            }
        };
        System.setOut(new PrintStream(centered, true, StandardCharsets.UTF_8));
        System.setProperty("foodease.centeredConsole", "true");
    }

    /** Opens a clean terminal screen and centers the welcome group vertically. */
    public static void beginWelcome(int welcomeLines) {
        if (System.console() != null) {
            alternateScreen = true;
            terminalOutput.print("\u001B[?1049h\u001B[?25l");
            terminalOutput.flush();
        }
        beginScreen(welcomeLines);
    }

    /** Clears the current page and vertically centers the next screen without a delay. */
    public static void beginScreen(int contentLines) {
        if (System.console() == null) return;
        Size current = refreshSize();
        int top = Math.max(1, (current.rows() - contentLines) / 2 + 1);
        terminalOutput.print("\u001B[?25l\u001B[2J\u001B[3J\u001B[H\u001B[" + top + ";1H\u001B[?25h");
        terminalOutput.flush();
    }

    public static void finishWelcome() {
        if (System.console() != null) {
            terminalOutput.print("\u001B[?25h");
            terminalOutput.flush();
        }
    }

    /** Prints the input marker without forcing the line-centering buffer to split a prompt. */
    public static void printPromptMarker() {
        if (System.console() == null) {
            System.out.print(ConsoleTheme.sage("> "));
            System.out.flush();
            return;
        }
        int left = Math.max(0, (size.columns() - ConsoleOutput.CONSOLE_WIDTH) / 2);
        terminalOutput.print("\u001B[" + (left + 2) + "G" + ConsoleTheme.sage("> "));
        terminalOutput.flush();
    }

    private static synchronized Size refreshSize() {
        long now = System.nanoTime();
        if (now - lastSizeCheck > 1_000_000_000L) {
            size = readTerminalSize();
            lastSizeCheck = now;
        }
        return size;
    }

    private static Size readTerminalSize() {
        int columns = 100;
        int rows = 40;
        try {
        Process process = new ProcessBuilder("powershell.exe", "-NoProfile", "-NonInteractive",
                    "-Command", "$window = $Host.UI.RawUI.WindowSize; [Console]::Out.WriteLine($window.Width.ToString() + ',' + $window.Height.ToString())")
                    .redirectErrorStream(true).start();
            if (process.waitFor(1200, TimeUnit.MILLISECONDS)) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        process.getInputStream(), StandardCharsets.UTF_8))) {
                    String output = reader.readLine();
                    if (output != null && output.matches("\\d+,\\d+")) {
                        String[] dimensions = output.split(",");
                        columns = Integer.parseInt(dimensions[0]);
                        rows = Integer.parseInt(dimensions[1]);
                    }
                }
            } else {
                process.destroyForcibly();
            }
        } catch (Exception ignored) {
            // Use a terminal-friendly fallback when the host does not expose its viewport.
        }
        return new Size(Math.max(columns, 1), Math.max(rows, 1));
    }

    private static void writeLine(PrintStream original, byte[] bytes) {
        // Keep line output immediate; refresh dimensions only when a new screen starts.
        Size current = size;
        String text = new String(bytes, StandardCharsets.UTF_8);
        String visible = text.replaceAll("\\u001B\\[[0-?]*[ -/]*[@-~]", "").replace("\r", "");
        int padding = Math.max(0, (current.columns() - visible.length()) / 2);
        original.print("\u001B[" + (padding + 1) + "G");
        original.write(bytes, 0, bytes.length);
        original.flush();
    }

    private record Size(int columns, int rows) { }
}
