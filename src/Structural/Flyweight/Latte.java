package Structural.Flyweight;

/**
 * Concrete Flyweight - Latte
 * Contains intrinsic state (shared among all instances)
 */
public class Latte implements ICoffee {
    // Intrinsic State (shared)
    private final String name = "Latte";
    private final double price = 20.0;
    private final String prepTime = "3 dakika";

    @Override
    public void serveCoffee(int tableNumber, int orderNumber) {
        // Extrinsic state (tableNumber, orderNumber) is passed as parameter
        System.out.println("Serving " + name + " to table " + tableNumber +
                         " (Order #" + orderNumber + ") - Price: " + price +
                         " TL, Prep time: " + prepTime);
    }
}
