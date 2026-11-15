
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Order order = new Order();

        System.out.println("======================================");
        System.out.println("          LING'S RESTAURANT");
        System.out.println("       RESTAURANT ORDERING SYSTEM");
        System.out.println("======================================");

        System.out.print("Enter Customer Name: ");
        String customerName = input.nextLine();

        System.out.println("\nWelcome, " + customerName + "!");

        int choice;

        do {
            Menu.displayMenu();

            System.out.print("\nEnter Food Number (0 to Checkout): ");
            choice = input.nextInt();

            if (choice >= 1 && choice <= Menu.foods.length) {

                System.out.print("Enter Quantity: ");
                int quantity = input.nextInt();

                if (quantity > 0) {
                    order.addOrder(choice - 1, quantity);
                    System.out.println("Order Added Successfully!");
                } else {
                    System.out.println("Invalid Quantity!");
                }

            } else if (choice != 0) {
                System.out.println("Invalid Choice!");
            }

        } while (choice != 0);

        double total = order.calculateTotal();

        if (total > 0) {
            System.out.printf("\nTotal Amount: PHP %.2f%n", total);

            double cash;

            do {
                System.out.print("Enter Cash Payment: PHP ");
                cash = input.nextDouble();

                if (cash < total) {
                    System.out.println("Insufficient payment! Please try again.");
                }

            } while (cash < total);

            order.displayReceipt(customerName, cash);

        } else {
            System.out.println("\nNo orders were placed.");
        }

        System.out.println("\nThank you for visiting LING'S RESTAURANT!");

        input.close();
    }
}
