package Prototype;

public class Car implements IPrototype {
    private String brand;
    private String model;
    private String color;
    private int year;

    public Car(String brand, String model, String color, int year) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
    }

    // Copy constructor - deep copy için kullanılacak
    private Car(Car car) {
        this.brand = car.brand;
        this.model = car.model;
        this.color = car.color;
        this.year = car.year;
    }

    @Override
    public IPrototype clone() {
        return new Car(this);
    }

    @Override
    public void displayInfo() {
        System.out.println("Car: " + year + " " + brand + " " + model + " - Color: " + color);
    }

    // Getters and Setters
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
