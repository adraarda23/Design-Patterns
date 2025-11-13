package Creational.AbstractFactory;

public class EkonomikPayment implements IPayment{
    @Override
    public void payment() {
        System.out.println("EkonomikPayment");
    }
}
