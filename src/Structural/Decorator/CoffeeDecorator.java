package Structural.Decorator;

/**
 * Base Decorator (Abstract Decorator)
 * Tüm concrete decorator'ların türediği soyut sınıf.
 *
 * ÖNEMLİ: Bu sınıf bir Coffee nesnesini sarar (wraps) ve
 * kendisi de Coffee interface'ini implement eder.
 * Bu sayede decorator'lar iç içe geçebilir (nested decorators).
 */
public abstract class CoffeeDecorator implements Coffee {

    // Sarmalanan (wrapped) kahve nesnesi
    protected Coffee decoratedCoffee;

    /**
     * Constructor - sarmalanacak kahveyi alır
     * @param coffee sarmalanacak kahve nesnesi
     */
    public CoffeeDecorator(Coffee coffee) {
        this.decoratedCoffee = coffee;
    }

    @Override
    public String getDescription() {
        // Varsayılan olarak sarmalanan kahvenin açıklamasını döndür
        return decoratedCoffee.getDescription();
    }

    @Override
    public double getCost() {
        // Varsayılan olarak sarmalanan kahvenin fiyatını döndür
        return decoratedCoffee.getCost();
    }
}
