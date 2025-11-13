package Structural.Flyweight;

/**
 * Flyweight Interface
 * Defines the operation that takes extrinsic state as parameters
 */
public interface ICoffee {
    void serveCoffee(int tableNumber, int orderNumber);
}
