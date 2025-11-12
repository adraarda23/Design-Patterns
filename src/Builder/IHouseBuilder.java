package Builder;

public interface IHouseBuilder {
    void buildFoundation();
    void buildStructure();
    void buildRoof();
    void buildInterior();
    void buildGarden();
    void buildGarage();
    void buildSwimmingPool();
    House getHouse();
}
