package Structural.Decorator;

/**
 * Concrete Component
 * Temel kahve implementasyonu.
 * Decorator'lar bu nesneyi sarmalayarak ek özellikler ekleyecek.
 */
public class SimpleCoffee implements Coffee {

    @Override
    public String getDescription() {
        return "Sade Kahve";
    }

    @Override
    public double getCost() {
        return 10.0; // Temel kahve fiyatı: 10 TL
    }
}
