package Structural.Decorator;

/**
 * Concrete Decorator - Krem Şanti Ekleyen
 * Kahveye krem şanti özelliği ekler.
 */
public class WhippedCreamDecorator extends CoffeeDecorator {

    public WhippedCreamDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Krem Şanti";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + 5.0; // Krem Şanti: +5 TL
    }
}
