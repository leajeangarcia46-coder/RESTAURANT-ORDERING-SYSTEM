# MediCare Pharmacy Customer Order System

A Java 21 console application with a pharmacy-style product catalog and checkout. The sample catalog and prices are for demonstration; product and payment operations remain the same as before.

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

Enter a customer name and choose in-store or pickup. Use **1** to add products, **2** to review the order, **3** to remove a product, **4** to check out, or **5** to cancel. Checkout accepts cash or card; cash orders ask for the amount received and print the change on the receipt. Prices display in Philippine pesos.

Product data is held in memory in `InMemoryMenuRepository` and resets when the application exits. The app does not manage prescription verification, inventory, or expiry tracking.
