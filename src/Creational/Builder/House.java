package Creational.Builder;

public class House {
    private String foundation;
    private String structure;
    private String roof;
    private String interior;
    private boolean hasGarden;
    private boolean hasGarage;
    private boolean hasSwimmingPool;
    private int rooms;

    public void setFoundation(String foundation) {
        this.foundation = foundation;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public void setRoof(String roof) {
        this.roof = roof;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    public void setHasGarage(boolean hasGarage) {
        this.hasGarage = hasGarage;
    }

    public void setHasSwimmingPool(boolean hasSwimmingPool) {
        this.hasSwimmingPool = hasSwimmingPool;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public void displayInfo() {
        System.out.println("\n=== House Details ===");
        System.out.println("Foundation: " + foundation);
        System.out.println("Structure: " + structure);
        System.out.println("Roof: " + roof);
        System.out.println("Interior: " + interior);
        System.out.println("Rooms: " + rooms);
        System.out.println("Has Garden: " + (hasGarden ? "Yes" : "No"));
        System.out.println("Has Garage: " + (hasGarage ? "Yes" : "No"));
        System.out.println("Has Swimming Pool: " + (hasSwimmingPool ? "Yes" : "No"));
    }
}
