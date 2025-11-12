package Singleton;

public class NaiveSingleton {

    private NaiveSingleton(){

    }

    private static NaiveSingleton instance;

    public static NaiveSingleton getInstance(){
        if(instance == null){
            System.out.println("yaratıldım");
            instance = new NaiveSingleton();
        }
        return instance;
    }



}
