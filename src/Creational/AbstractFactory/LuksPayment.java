package Creational.AbstractFactory;

public class LuksPayment implements IPayment {
    @Override
    public void payment() {
        System.out.println("LuksPayment");
    }
}
