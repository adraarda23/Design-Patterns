package Structural.Flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight Factory
 * Manages and caches flyweight objects to ensure sharing
 */
public class CoffeeFactory {
    // Cache for storing flyweight objects
    private static Map<String, ICoffee> coffeeMap = new HashMap<>();

    /**
     * Returns a shared flyweight object
     * Creates new object only if it doesn't exist in cache
     */
    public static ICoffee getCoffee(String type) {
        ICoffee coffee = coffeeMap.get(type);

        if (coffee == null) {
            // Create new flyweight object if not exists
            switch (type.toLowerCase()) {
                case "espresso":
                    coffee = new Espresso();
                    break;
                case "latte":
                    coffee = new Latte();
                    break;
                case "cappuccino":
                    coffee = new Cappuccino();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown coffee type: " + type);
            }

            // Cache the newly created object
            coffeeMap.put(type, coffee);
            System.out.println("Creating new " + type + " object");
        } else {
            System.out.println("Reusing existing " + type + " object");
        }

        return coffee;
    }

    /**
     * Returns the number of flyweight objects created
     */
    public static int getTotalCoffeeTypesMade() {
        return coffeeMap.size();
    }
}
