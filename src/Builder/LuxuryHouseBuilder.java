package Builder;

public class LuxuryHouseBuilder implements IHouseBuilder {
    private House house;

    public LuxuryHouseBuilder() {
        this.house = new House();
    }

    @Override
    public void buildFoundation() {
        house.setFoundation("Reinforced concrete foundation with steel beams");
        System.out.println("Building luxury foundation...");
    }

    @Override
    public void buildStructure() {
        house.setStructure("Premium brick and marble structure");
        house.setRooms(8);
        System.out.println("Building luxury structure...");
    }

    @Override
    public void buildRoof() {
        house.setRoof("Ceramic tile roof with solar panels");
        System.out.println("Building luxury roof...");
    }

    @Override
    public void buildInterior() {
        house.setInterior("Luxury interior with marble floors and designer furniture");
        System.out.println("Building luxury interior...");
    }

    @Override
    public void buildGarden() {
        house.setHasGarden(true);
        System.out.println("Adding luxury garden with fountain...");
    }

    @Override
    public void buildGarage() {
        house.setHasGarage(true);
        System.out.println("Adding triple garage...");
    }

    @Override
    public void buildSwimmingPool() {
        house.setHasSwimmingPool(true);
        System.out.println("Adding Olympic-size swimming pool...");
    }

    @Override
    public House getHouse() {
        return this.house;
    }
}
