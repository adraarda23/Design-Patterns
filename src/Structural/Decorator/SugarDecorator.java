package Structural.Decorator;

/**
 * Concrete Decorator - Şeker Ekleyen
 * Kahveye şeker özelliği ekler.
 */
public class SugarDecorator extends CoffeeDecorator {

    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Şeker";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + 1.5; // Şeker: +1.5 TL
    }
}
