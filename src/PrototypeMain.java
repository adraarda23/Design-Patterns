import Prototype.Car;
import Prototype.Computer;
import Prototype.IPrototype;

public class PrototypeMain {
    public static void main(String[] args) {

        System.out.println("=== Prototype Design Pattern Demo ===\n");

        // Orijinal Car nesnesi oluştur
        System.out.println("--- Car Cloning Demo ---");
        Car originalCar = new Car("BMW", "M5", "Black", 2023);
        System.out.print("Original Car: ");
        originalCar.displayInfo();

        // Car nesnesini clone et
        Car clonedCar = (Car) originalCar.clone();
        System.out.print("Cloned Car: ");
        clonedCar.displayInfo();

        // Clone edilen nesnenin özelliklerini değiştir
        clonedCar.setColor("Red");
        clonedCar.setYear(2024);

        System.out.println("\nAfter modifying cloned car:");
        System.out.print("Original Car: ");
        originalCar.displayInfo();
        System.out.print("Modified Cloned Car: ");
        clonedCar.displayInfo();

        System.out.println("\n--- Computer Cloning Demo ---");
        // Orijinal Computer nesnesi oluştur
        Computer originalComputer = new Computer("Intel i9", 32, 1024, "Windows 11");
        System.out.print("Original Computer: ");
        originalComputer.displayInfo();

        // Computer nesnesini clone et
        Computer clonedComputer = (Computer) originalComputer.clone();
        System.out.print("Cloned Computer: ");
        clonedComputer.displayInfo();

        // Clone edilen nesnenin özelliklerini değiştir
        clonedComputer.setRam(64);
        clonedComputer.setOperatingSystem("Ubuntu 22.04");

        System.out.println("\nAfter modifying cloned computer:");
        System.out.print("Original Computer: ");
        originalComputer.displayInfo();
        System.out.print("Modified Cloned Computer: ");
        clonedComputer.displayInfo();

        // Nesne referanslarının farklı olduğunu göster
        System.out.println("\n--- Object Reference Comparison ---");
        System.out.println("Are they the same object? " + (originalCar == clonedCar));
        System.out.println("Original Car hashCode: " + originalCar.hashCode());
        System.out.println("Cloned Car hashCode: " + clonedCar.hashCode());
    }
}
