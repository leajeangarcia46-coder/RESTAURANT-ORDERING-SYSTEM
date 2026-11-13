public class Menu{

    public static String[] foods = {
        "Chicken Adobo",
        "Fried Chicken",
        "Pork Sinigang",
        "Beef Steak",
        "Pancit Canton",
        "Hamburger",
        "French Fries",
        "Soft Drinks"
    };

    public static double[] prices = {
        120, 100, 130, 150, 80, 75, 50, 25
    };

    public static void displayMenu() {
        System.out.println("\n========== FOOD MENU ==========");

        for (int i = 0; i < foods.length; i++) {
            System.out.printf("%d. %-20s PHP %.2f%n",
                    i + 1, foods[i], prices[i]);
        }

        System.out.println("0. Checkout");
        System.out.println("===============================");
    }
}

