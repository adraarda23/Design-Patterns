package FactoryMethod;

public class FemaleFactory implements IHumanFactory{
    @Override
    public IHuman createHuman() {
        return new Female();
    }
}
