

import java.util.ArrayList;

public class Order {

    private ArrayList<String> foods = new ArrayList<>();
    private ArrayList<Integer> quantities = new ArrayList<>();
    private ArrayList<Double> subtotals = new ArrayList<>();

    public void addOrder(int index, int quantity) {

        String food = Menu.foods[index];
        double price = Menu.prices[index];

        foods.add(food);
        quantities.add(quantity);
        subtotals.add(price * quantity);
    }

    public void displayReceipt(String customerName) {

        double total = 0;

        System.out.println("\n========== ORDER RECEIPT ==========");
        System.out.println("Customer Name: " + customerName);
        System.out.println("-----------------------------------");

        for (int i = 0; i < foods.size(); i++) {

            System.out.printf(
                "%-20s x%d PHP %.2f%n",
                foods.get(i),
                quantities.get(i),
                subtotals.get(i)
            );

            total += subtotals.get(i);
        }

        System.out.println("-----------------------------------");
        System.out.printf("TOTAL: PHP %.2f%n", total);
        System.out.println("===================================");
    }
}
