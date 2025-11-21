package Structural.Decorator;

/**
 * Concrete Decorator - Süt Ekleyen
 * Kahveye süt özelliği ekler.
 * Hem açıklamayı hem de fiyatı günceller.
 */
public class MilkDecorator extends CoffeeDecorator {

    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        // Mevcut açıklamaya süt ekle
        return decoratedCoffee.getDescription() + ", Süt";
    }

    @Override
    public double getCost() {
        // Mevcut fiyata süt ücreti ekle
        return decoratedCoffee.getCost() + 3.0; // Süt: +3 TL
    }
}
