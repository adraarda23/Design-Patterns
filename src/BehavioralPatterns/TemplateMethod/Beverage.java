package BehavioralPatterns.TemplateMethod;
abstract class Beverage {
    final void prepare() {
        boilWater();
        brew();
        pourInCup();
        addCondiments();
    }

    void boilWater() { System.out.println("Suyu kaynat"); }
    void pourInCup() { System.out.println("Bardağa dök"); }

    abstract void brew();
    abstract void addCondiments();
}

class Tea extends Beverage {
    void brew() { System.out.println("Çayı demle"); }
    void addCondiments() { System.out.println("Limon ekle"); }
}

class Coffee extends Beverage {
    void brew() { System.out.println("Kahveyi süz"); }
    void addCondiments() { System.out.println("Süt ekle"); }


    public static void main(String[] args){
        Beverage tea = new Tea();
        Beverage coffee = new Coffee();
        tea.prepare();
        System.out.println("\n=============\n");
        coffee.prepare();

    }
}