package FactoryMethod;

public class Female implements IHuman{
    @Override
    public void saySomething() {
        System.out.println("FemaleFactory saySomething");
    }

}
