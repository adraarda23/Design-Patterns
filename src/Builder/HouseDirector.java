package Builder;

public class HouseDirector {
    private IHouseBuilder houseBuilder;

    public HouseDirector(IHouseBuilder houseBuilder) {
        this.houseBuilder = houseBuilder;
    }

    public void setHouseBuilder(IHouseBuilder houseBuilder) {
        this.houseBuilder = houseBuilder;
    }

    // Tam özellikli ev inşa et
    public House constructFullHouse() {
        System.out.println("\nConstructing full house...");
        houseBuilder.buildFoundation();
        houseBuilder.buildStructure();
        houseBuilder.buildRoof();
        houseBuilder.buildInterior();
        houseBuilder.buildGarden();
        houseBuilder.buildGarage();
        houseBuilder.buildSwimmingPool();
        return houseBuilder.getHouse();
    }

    // Sadece temel ev inşa et (ekstralar olmadan)
    public House constructBasicHouse() {
        System.out.println("\nConstructing basic house...");
        houseBuilder.buildFoundation();
        houseBuilder.buildStructure();
        houseBuilder.buildRoof();
        houseBuilder.buildInterior();
        return houseBuilder.getHouse();
    }
}
