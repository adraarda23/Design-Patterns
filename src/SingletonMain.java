import Singleton.EnumSingleton;
import Singleton.NaiveSingleton;

public class SingletonMain {
    public static void main(String[] args) {


        //NAIVE
        NaiveSingleton ns = NaiveSingleton.getInstance();
        NaiveSingleton ns2 = NaiveSingleton.getInstance();


        //ENUM
        EnumSingleton a1 = EnumSingleton.INSTANCE;
        EnumSingleton a2 = EnumSingleton.INSTANCE;

        a1.doSomething();
        a2.doSomething();







    }
}