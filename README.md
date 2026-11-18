# FoodEase Restaurant Ordering System

A Java 21 console application for dine-in and takeout restaurant orders, with an editable cart, kitchen instructions, cash/card payment recording, and printed receipts.

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

Enter a customer name and choose dine-in or takeout. Dine-in orders request a table number, and each order can include optional kitchen instructions. Use **1** to enter add mode; the menu appears once and you can add multiple items before entering **B**. Use **2** to review the order, **3** to remove an item, **4** to check out, or **5** to cancel. Checkout accepts cash or card; cash orders ask for the amount received and print the change on the receipt.

Menu data is held in memory in `InMemoryMenuRepository` and resets when the application exits. Card payments are recorded in the console and are not processed by a payment provider.
