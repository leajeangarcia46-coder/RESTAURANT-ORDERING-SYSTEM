# Restaurant Ordering System

A Java 21 console application for viewing a menu, placing restaurant orders, and calculating payments. It helps staff manage customer orders and checkout.

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

Enter a customer name and choose dine-in or takeout. Use **A** to add menu items, **V** to review the cart, **R** to remove an item, **C** to check out, or **X** to cancel. Checkout accepts cash or card; cash orders ask for the amount received and print the change on the receipt.

Menu data is held in memory in `InMemoryMenuRepository` and resets when the application exits.
