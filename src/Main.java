
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Order order = new Order();

        System.out.println("======================================");
        System.out.println("       LING'S RESTAURANT");
        System.out.println("       RESTAURANT ORDERING SYSTEM");
        System.out.println("======================================");

        System.out.print("Enter customer name: ");
        String customerName = input.nextLine();

        System.out.println("\nWelcome, " + customerName + "!");

        boolean running = true;

        while (running) {
            Menu.displayMenu();

            System.out.print("Enter your choice (0 to checkout): ");
            int choice = input.nextInt();

            if (choice == 0) {
                running = false;
                break;
            }

            if (choice < 1 || choice > Menu.foods.length) {
                System.out.println("Invalid choice!");
                continue;
            }

            System.out.print("Enter quantity: ");
            int quantity = input.nextInt();

            if (quantity <= 0) {
                System.out.println("Invalid quantity!");
                continue;
            }

            order.addOrder(choice - 1, quantity);
            System.out.println("Order successfully added!");
        }

        order.displayReceipt(customerName);

        System.out.println("\nThank you for visiting LING'S RESTAURANT!");
        input.close();
    }
}
