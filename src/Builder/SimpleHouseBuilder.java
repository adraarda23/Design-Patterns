package Builder;

public class SimpleHouseBuilder implements IHouseBuilder {
    private House house;

    public SimpleHouseBuilder() {
        this.house = new House();
    }

    @Override
    public void buildFoundation() {
        house.setFoundation("Basic concrete foundation");
        System.out.println("Building simple foundation...");
    }

    @Override
    public void buildStructure() {
        house.setStructure("Standard brick structure");
        house.setRooms(3);
        System.out.println("Building simple structure...");
    }

    @Override
    public void buildRoof() {
        house.setRoof("Standard tile roof");
        System.out.println("Building simple roof...");
    }

    @Override
    public void buildInterior() {
        house.setInterior("Simple interior with laminate floors");
        System.out.println("Building simple interior...");
    }

    @Override
    public void buildGarden() {
        house.setHasGarden(true);
        System.out.println("Adding small garden...");
    }

    @Override
    public void buildGarage() {
        house.setHasGarage(false);
        System.out.println("No garage added...");
    }

    @Override
    public void buildSwimmingPool() {
        house.setHasSwimmingPool(false);
        System.out.println("No swimming pool added...");
    }

    @Override
    public House getHouse() {
        return this.house;
    }
}
