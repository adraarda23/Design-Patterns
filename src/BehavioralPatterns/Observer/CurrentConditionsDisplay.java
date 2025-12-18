package BehavioralPatterns.Observer;

/**
 * Concrete Observer - Mevcut Durum Ekranı
 * Güncel hava durumu bilgilerini gösterir
 */
public class CurrentConditionsDisplay implements Observer {
    private float temperature;
    private float humidity;
    private Subject weatherStation;

    public CurrentConditionsDisplay(Subject weatherStation) {
        this.weatherStation = weatherStation;
        weatherStation.registerObserver(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }

    public void display() {
        System.out.println("\n📺 [Mevcut Durum Ekranı]");
        System.out.println("   Sıcaklık: " + temperature + "°C");
        System.out.println("   Nem: " + humidity + "%");
    }
}
