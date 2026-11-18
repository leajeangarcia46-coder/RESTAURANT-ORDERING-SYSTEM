# FoodEase Restaurant Ordering System

A Java 21 console application for dine-in and takeout restaurant orders, with a categorized menu, editable cart, item notes, checkout totals, payment recording, and printable receipts.

## Run in NetBeans

Open this project in NetBeans and choose **Run Project**. The configured entry point is `com.foodease.Main`.

## Run from a terminal

From the project directory, compile and run with:

```powershell
$sources = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d build/classes $sources
java -cp build/classes com.foodease.Main
```

## Order flow

Enter a customer name and choose dine-in or takeout. Dine-in orders request a table number. Use **1** to enter add mode; the menu is grouped by category and you can add multiple items before entering **B**. Each item can include an optional special request. Use **2** to review the cart, **3** to remove a cart line, **4** to change its quantity, **5** to check out, or **6** to cancel. Checkout accepts cash, card, or digital wallet. Cash orders ask for the amount received and show change on the receipt. A timestamped receipt includes an order number, item details, subtotal, tax, service charge, and total. After checkout, choose **Y** at the paper receipt prompt to select a printer.

The default pricing policy assumes menu prices include 12% VAT and applies no service charge. Adjust the policy passed to `OrderService` to use the rates and tax treatment configured for your restaurant. This is a sample application setting; confirm the appropriate treatment for your business.

Menu data is held in memory in `InMemoryMenuRepository` and resets when the application exits. Card and wallet payments are recorded in the console and are not processed by a payment provider.
