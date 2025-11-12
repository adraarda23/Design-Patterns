package Singleton;

public enum EnumSingleton {
    INSTANCE; // tek örnek burada

    // örnek bir alan
    private int counter = 0;

    // örnek bir metot
    public void doSomething() {
        counter++;
        System.out.println("Çalıştı! Sayaç = " + counter);
    }
}