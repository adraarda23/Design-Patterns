package BehavioralPatterns.Observer;

/**
 * Concrete Observer - İstatistik Ekranı
 * Min, max ve ortalama sıcaklık istatistiklerini gösterir
 */
public class StatisticsDisplay implements Observer {
    private float maxTemp = Float.MIN_VALUE;
    private float minTemp = Float.MAX_VALUE;
    private float tempSum = 0.0f;
    private int numReadings = 0;
    private Subject weatherStation;

    public StatisticsDisplay(Subject weatherStation) {
        this.weatherStation = weatherStation;
        weatherStation.registerObserver(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        tempSum += temperature;
        numReadings++;

        if (temperature > maxTemp) {
            maxTemp = temperature;
        }

        if (temperature < minTemp) {
            minTemp = temperature;
        }

        display();
    }

    public void display() {
        System.out.println("\n📊 [İstatistik Ekranı]");
        System.out.println("   Ortalama Sıcaklık: " + (tempSum / numReadings) + "°C");
        System.out.println("   Minimum Sıcaklık: " + minTemp + "°C");
        System.out.println("   Maksimum Sıcaklık: " + maxTemp + "°C");
    }
}
