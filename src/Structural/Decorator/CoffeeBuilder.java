package Structural.Decorator;

/**
 * Builder sınıfı - Decorator'ları daha temiz şekilde uygulamak için.
 * Method chaining ile akıcı (fluent) API sağlar.
 */
public class CoffeeBuilder {

    private Coffee coffee;

    public CoffeeBuilder() {
        this.coffee = new SimpleCoffee();
    }

    public CoffeeBuilder withMilk() {
        coffee = new MilkDecorator(coffee);
        return this;
    }

    public CoffeeBuilder withSugar() {
        coffee = new SugarDecorator(coffee);
        return this;
    }

    public CoffeeBuilder withWhippedCream() {
        coffee = new WhippedCreamDecorator(coffee);
        return this;
    }

    public Coffee build() {
        return coffee;
    }
}
