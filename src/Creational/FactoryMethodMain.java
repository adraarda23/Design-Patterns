package Creational;

import Creational.FactoryMethod.Female;
import Creational.FactoryMethod.IHuman;
import Creational.FactoryMethod.Male;

public class FactoryMethodMain {

    public static void main(String[] args) {
        IHuman human = new Male();
        IHuman human2 = new Female();

        human.saySomething();
        human2.saySomething();
    }


}
