
import java.util.ArrayList;

public class Order {

    private ArrayList<String> foods;
    private ArrayList<Integer> quantities;
    private ArrayList<Double> subtotals;

    public Order() {
        foods = new ArrayList<>();
        quantities = new ArrayList<>();
        subtotals = new ArrayList<>();
    }

    public void addOrder(int index, int quantity) {

        if (index < 0 || index >= Menu.foods.length) {
            System.out.println("Invalid food selection!");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Invalid quantity!");
            return;
        }

        String food = Menu.foods[index];
        double price = Menu.prices[index];

        foods.add(food);
        quantities.add(quantity);
        subtotals.add(price * quantity);
    }

    public void displayReceipt() {

        double total = 0;

        System.out.println("\n========== ORDER RECEIPT ==========");

        if (foods.isEmpty()) {
            System.out.println("No orders placed.");
        }

        for (int i = 0; i < foods.size(); i++) {

            System.out.printf("%-20s x%d PHP %.2f%n",
                    foods.get(i),
                    quantities.get(i),
                    subtotals.get(i));

            total += subtotals.get(i);
        }

        System.out.println("-----------------------------------");
        System.out.printf("TOTAL: PHP %.2f%n", total);
        System.out.println("===================================");
    }
}
