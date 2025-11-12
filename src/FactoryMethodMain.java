import FactoryMethod.Female;
import FactoryMethod.IHuman;
import FactoryMethod.Male;

public class FactoryMethodMain {

    public static void main(String[] args) {
        IHuman human = new Male();
        IHuman human2 = new Female();

        human.saySomething();
        human2.saySomething();
    }


}
