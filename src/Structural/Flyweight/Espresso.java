package Structural.Flyweight;

/**
 * Concrete Flyweight - Espresso
 * Contains intrinsic state (shared among all instances)
 */
public class Espresso implements ICoffee {
    // Intrinsic State (shared)
    private final String name = "Espresso";
    private final double price = 15.0;
    private final String prepTime = "2 dakika";

    @Override
    public void serveCoffee(int tableNumber, int orderNumber) {
        // Extrinsic state (tableNumber, orderNumber) is passed as parameter
        System.out.println("Serving " + name + " to table " + tableNumber +
                         " (Order #" + orderNumber + ") - Price: " + price +
                         " TL, Prep time: " + prepTime);
    }
}
