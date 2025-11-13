package Creational;

import Creational.Builder.*;

public class BuilderMain {
    public static void main(String[] args) {

        System.out.println("=== Builder Design Pattern Demo ===");

        // Senaryo 1: Director ile Luxury House inşa et
        System.out.println("\n--- Scenario 1: Building Luxury House with Director ---");
        IHouseBuilder luxuryBuilder = new LuxuryHouseBuilder();
        HouseDirector director = new HouseDirector(luxuryBuilder);
        House luxuryHouse = director.constructFullHouse();
        luxuryHouse.displayInfo();

        // Senaryo 2: Director ile Simple House inşa et (sadece temel özellikler)
        System.out.println("\n--- Scenario 2: Building Simple House (Basic) with Director ---");
        IHouseBuilder simpleBuilder = new SimpleHouseBuilder();
        director.setHouseBuilder(simpleBuilder);
        House simpleHouse = director.constructBasicHouse();
        simpleHouse.displayInfo();

        // Senaryo 3: Director olmadan manuel inşa (özelleştirilmiş)
        System.out.println("\n--- Scenario 3: Building Custom House without Director ---");
        IHouseBuilder customBuilder = new LuxuryHouseBuilder();
        System.out.println("\nBuilding custom luxury house (only some features)...");
        customBuilder.buildFoundation();
        customBuilder.buildStructure();
        customBuilder.buildRoof();
        customBuilder.buildInterior();
        customBuilder.buildGarden();
        // Garage ve swimming pool eklenmedi
        House customHouse = customBuilder.getHouse();
        customHouse.displayInfo();

        System.out.println("\n=== Builder Pattern Benefits ===");
        System.out.println("1. Complex object construction is separated from representation");
        System.out.println("2. Same construction process can create different representations");
        System.out.println("3. Step-by-step construction with control over the process");
        System.out.println("4. Director can encapsulate complex construction logic");
    }
}
