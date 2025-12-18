package BehavioralPatterns.Observer;

/**
 * Concrete Observer - Tahmin Ekranı
 * Basınç değişimlerine göre hava durumu tahmini yapar
 */
public class ForecastDisplay implements Observer {
    private float currentPressure = 1013.0f;  // Standart atmosfer basıncı
    private float lastPressure;
    private Subject weatherStation;

    public ForecastDisplay(Subject weatherStation) {
        this.weatherStation = weatherStation;
        weatherStation.registerObserver(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        lastPressure = currentPressure;
        currentPressure = pressure;
        display();
    }

    public void display() {
        System.out.println("\n🔮 [Hava Tahmini Ekranı]");

        if (currentPressure > lastPressure) {
            System.out.println("   Hava durumu iyileşiyor! ☀️");
        } else if (currentPressure < lastPressure) {
            System.out.println("   Hava bozacak gibi görünüyor! 🌧️");
        } else {
            System.out.println("   Hava durumu aynı kalacak. ⛅");
        }

        System.out.println("   Mevcut Basınç: " + currentPressure + " hPa");
    }
}
