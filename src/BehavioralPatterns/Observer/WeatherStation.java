package BehavioralPatterns.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Subject - Hava Durumu İstasyonu
 * Hava durumu verilerini tutar ve değiştiğinde tüm observer'ları bilgilendirir
 */
public class WeatherStation implements Subject {
    private List<Observer> observers;
    private float temperature;
    private float humidity;
    private float pressure;

    public WeatherStation() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("✅ Yeni gözlemci eklendi: " + observer.getClass().getSimpleName());
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observers.remove(observer)) {
            System.out.println("❌ Gözlemci çıkarıldı: " + observer.getClass().getSimpleName());
        }
    }

    @Override
    public void notifyObservers() {
        System.out.println("\n📢 Tüm gözlemciler bilgilendiriliyor...");
        for (Observer observer : observers) {
            observer.update(temperature, humidity, pressure);
        }
    }

    /**
     * Hava durumu verileri değiştiğinde çağrılır
     * Otomatik olarak tüm observer'ları bilgilendirir
     */
    public void setMeasurements(float temperature, float humidity, float pressure) {
        System.out.println("\n🌡️  Yeni hava durumu verileri alındı!");
        System.out.println("   Sıcaklık: " + temperature + "°C");
        System.out.println("   Nem: " + humidity + "%");
        System.out.println("   Basınç: " + pressure + " hPa");

        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;

        // Durum değişti, tüm observer'ları bilgilendir
        notifyObservers();
    }

    // Getter metodları
    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }
}
