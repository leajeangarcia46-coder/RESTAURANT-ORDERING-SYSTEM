package com.foodease.console;

import com.foodease.domain.Order;
import com.foodease.domain.OrderItem;
import com.foodease.domain.OrderType;
import com.foodease.domain.Receipt;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReceiptPrinter {
    public boolean printReceipt(Receipt receipt, Order order) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("FoodEase Receipt " + receipt.receiptNumber());
        job.setPrintable(createPrintable(buildLines(receipt, order)));
        if (!job.printDialog()) return false;
        job.print();
        return true;
    }

    private List<String> buildLines(Receipt receipt, Order order) {
        List<String> lines = new ArrayList<>();
        lines.add("FOODEASE RESTAURANT");
        lines.add("SALES RECEIPT");
        lines.add("----------------------------------------");
        lines.add("Order: " + receipt.receiptNumber());
        lines.add("Date: " + receipt.issuedAt().toString().replace('T', ' '));
        lines.add("Customer: " + receipt.customer().name());
        lines.add("Type: " + receipt.orderType().displayName());
        if (receipt.orderType() == OrderType.DINE_IN) lines.add("Table: " + order.tableNumber());
        lines.add("----------------------------------------");
        for (OrderItem item : order.items()) {
            lines.add(item.quantity() + " x " + item.menuItem().name());
            lines.add("    " + ConsoleLayout.money(item.unitPrice()) + " each = "
                    + ConsoleLayout.money(item.subtotal()));
            if (!item.note().isBlank()) lines.add("    Special request: " + item.note());
        }
        lines.add("----------------------------------------");
        lines.add("Subtotal: " + ConsoleLayout.money(receipt.subtotal()));
        lines.add((receipt.taxIncludedInPrices() ? "Tax included: " : "Tax: ")
                + ConsoleLayout.money(receipt.tax()));
        lines.add("Service charge: " + ConsoleLayout.money(receipt.serviceCharge()));
        lines.add("TOTAL: " + ConsoleLayout.money(receipt.total()));
        lines.add("Payment: " + receipt.payment().method().toUpperCase(Locale.ROOT));
        if (receipt.payment().method().equals("cash")) {
            lines.add("Cash received: " + ConsoleLayout.money(receipt.payment().amountTendered()));
            lines.add("Change: " + ConsoleLayout.money(receipt.payment().changeDue()));
        }
        lines.add("");
        lines.add("Thank you for dining with us!");
        return lines;
    }

    private Printable createPrintable(List<String> lines) {
        return (Graphics graphics, PageFormat pageFormat, int pageIndex) -> {
            Graphics2D g = (Graphics2D) graphics.create();
            try {
                g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
                int lineHeight = g.getFontMetrics().getHeight() + 3;
                int linesPerPage = Math.max(1, (int) (pageFormat.getImageableHeight() / lineHeight));
                int start = pageIndex * linesPerPage;
                if (start >= lines.size()) return Printable.NO_SUCH_PAGE;
                int end = Math.min(lines.size(), start + linesPerPage);
                int y = g.getFontMetrics().getAscent();
                for (int i = start; i < end; i++, y += lineHeight) {
                    g.drawString(lines.get(i), 0, y);
                }
                return Printable.PAGE_EXISTS;
            } finally {
                g.dispose();
            }
        };
    }

}
