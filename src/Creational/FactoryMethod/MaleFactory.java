package Creational.FactoryMethod;

public class MaleFactory implements IHumanFactory {

    @Override
    public IHuman createHuman() {
        return new Male();
    }
}
