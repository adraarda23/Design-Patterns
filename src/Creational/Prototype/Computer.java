package Creational.Prototype;

public class Computer implements IPrototype {
    private String processor;
    private int ram;
    private int storage;
    private String operatingSystem;

    public Computer(String processor, int ram, int storage, String operatingSystem) {
        this.processor = processor;
        this.ram = ram;
        this.storage = storage;
        this.operatingSystem = operatingSystem;
    }

    // Copy constructor - deep copy için kullanılacak
    private Computer(Computer computer) {
        this.processor = computer.processor;
        this.ram = computer.ram;
        this.storage = computer.storage;
        this.operatingSystem = computer.operatingSystem;
    }

    @Override
    public IPrototype clone() {
        return new Computer(this);
    }

    @Override
    public void displayInfo() {
        System.out.println("Computer: " + processor + ", " + ram + "GB RAM, " +
                          storage + "GB Storage, OS: " + operatingSystem);
    }

    // Getters and Setters
    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }
}
