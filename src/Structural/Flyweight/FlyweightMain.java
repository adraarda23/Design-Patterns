package Structural.Flyweight;

public class FlyweightMain {
    public static void main(String[] args) {

        System.out.println("=== Flyweight Design Pattern Demo ===");
        System.out.println("=== Coffee Shop Order System ===\n");

        // Simulate 10 orders
        // Notice: Multiple orders for same coffee types, but only 3 objects will be created

        System.out.println("--- Taking Orders ---\n");

        // Order 1: Espresso for table 1
        ICoffee order1 = CoffeeFactory.getCoffee("Espresso");
        order1.serveCoffee(1, 101);

        // Order 2: Latte for table 2
        ICoffee order2 = CoffeeFactory.getCoffee("Latte");
        order2.serveCoffee(2, 102);

        // Order 3: Espresso for table 3 (reuses Espresso object)
        ICoffee order3 = CoffeeFactory.getCoffee("Espresso");
        order3.serveCoffee(3, 103);

        // Order 4: Cappuccino for table 4
        ICoffee order4 = CoffeeFactory.getCoffee("Cappuccino");
        order4.serveCoffee(4, 104);

        // Order 5: Latte for table 5 (reuses Latte object)
        ICoffee order5 = CoffeeFactory.getCoffee("Latte");
        order5.serveCoffee(5, 105);

        // Order 6: Espresso for table 1 again
        ICoffee order6 = CoffeeFactory.getCoffee("Espresso");
        order6.serveCoffee(1, 106);

        // Order 7: Cappuccino for table 2 (reuses Cappuccino object)
        ICoffee order7 = CoffeeFactory.getCoffee("Cappuccino");
        order7.serveCoffee(2, 107);

        // Order 8: Latte for table 6
        ICoffee order8 = CoffeeFactory.getCoffee("Latte");
        order8.serveCoffee(6, 108);

        // Order 9: Espresso for table 7
        ICoffee order9 = CoffeeFactory.getCoffee("Espresso");
        order9.serveCoffee(7, 109);

        // Order 10: Cappuccino for table 8
        ICoffee order10 = CoffeeFactory.getCoffee("Cappuccino");
        order10.serveCoffee(8, 110);

        System.out.println("\n--- Summary ---");
        System.out.println("Total coffee types created: " + CoffeeFactory.getTotalCoffeeTypesMade());
        System.out.println("Total orders served: 10");

        System.out.println("\n=== Flyweight Pattern Benefits ===");
        System.out.println("1. Memory savings: Only 3 objects created for 10 orders");
        System.out.println("2. Intrinsic state (coffee properties) is shared");
        System.out.println("3. Extrinsic state (table & order numbers) is passed as parameters");
        System.out.println("4. Objects are reused instead of creating new instances");

        // Demonstrate that same object is reused
        System.out.println("\n--- Object Identity Test ---");
        ICoffee espresso1 = CoffeeFactory.getCoffee("Espresso");
        ICoffee espresso2 = CoffeeFactory.getCoffee("Espresso");
        System.out.println("Are both Espresso references the same object? " + (espresso1 == espresso2));
    }
}
