# FoodEase Restaurant Ordering and Checkout System

## About the project

FoodEase is a Java console application I made for my Object-Oriented Programming project. It helps a customer place a dine-in or takeout order, review and update the cart, pay for the order, and receive a receipt. The terminal screens use a centered layout and simple colors to make the steps easier to follow.

## What the system can do

- Create dine-in orders with a table number or takeout orders.
- Display the restaurant menu by category and price.
- Add several menu items, choose quantities, and enter optional special requests.
- View the cart, remove an item, or change its quantity.
- Check out using cash, card, or digital wallet.
- Validate cash received and calculate change.
- Show an itemized receipt and optionally open the computer's print dialog.

## Requirements

- Java Development Kit (JDK) 21
- Apache NetBeans, if you want to open and run the project in the IDE
- Windows for the included `run.bat` launcher and its colored, centered terminal display

## Run the project

### Using NetBeans

Open the project folder in NetBeans and choose **Run Project**. The project entry point is `com.foodease.Main`, and the configured run action launches `run.bat`.

### Using the Windows launcher

Open a terminal in the project folder and run:

```bat
run.bat
```

The launcher compiles the Java source and starts FoodEase. Keep the terminal open while placing the order.

### Compile and run manually

From the project folder in PowerShell:

```powershell
javac -encoding UTF-8 -sourcepath src -d build\classes src\com\foodease\Main.java
java -cp build\classes com.foodease.Main
```

## How to place an order

1. Enter a customer name.
2. Choose **1** for dine-in or **2** for takeout. Dine-in orders also ask for a table number.
3. Choose **1** from the order options to add items. Enter a menu number, quantity, and optional request. Enter **B** when you are finished adding items.
4. Use the order options to review the cart (**2**), remove an item (**3**), change a quantity (**4**), check out (**5**), or cancel (**6**).
5. At checkout, choose cash, card, or wallet. Cash orders ask how much was received.
6. Review the receipt. Answer **Y** if you want to open the printer dialog.

## Object-oriented design

I organized the code into packages so each part has a clear job:

- `com.foodease.console` handles keyboard input, terminal screens, layout, and receipt printing.
- `com.foodease.application` coordinates order operations and calculates checkout totals.
- `com.foodease.domain` contains the customer, order, menu, bill, payment, and receipt data.
- `com.foodease.data` provides the menu repository and its in-memory menu.

The project applies the main OOP ideas:

- **Abstraction:** `MenuRepository` and `PaymentMethod` describe the operations the rest of the program needs.
- **Inheritance:** `CashPaymentMethod` and `ElectronicPaymentMethod` extend the shared `PaymentMethod` type.
- **Polymorphism:** each payment method handles checkout through its own `charge` implementation.
- **Encapsulation:** domain objects validate their data, and service classes keep their internal state private.

## Current limitations

- Menu and order data are kept in memory, so they reset when the application closes.
- Card and wallet payments are recorded by the program; they are not sent to a real payment provider.
- The default pricing policy treats menu prices as including 12% VAT and adds no service charge. This is a project default and may need to be changed for a real restaurant.
- The centered color display is intended for a Windows terminal that supports ANSI colors. Other terminals may display the same content without color or centering.
