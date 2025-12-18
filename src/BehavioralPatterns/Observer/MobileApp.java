package BehavioralPatterns.Observer;

/**
 * Concrete Observer - Mobil Uygulama
 * Kullanıcının telefonuna bildirim gönderir
 */
public class MobileApp implements Observer {
    private String userName;
    private Subject weatherStation;

    public MobileApp(String userName, Subject weatherStation) {
        this.userName = userName;
        this.weatherStation = weatherStation;
        weatherStation.registerObserver(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        display(temperature, humidity, pressure);
    }

    public void display(float temperature, float humidity, float pressure) {
        System.out.println("\n📱 [" + userName + "'in Mobil Uygulaması]");
        System.out.println("   🔔 Bildirim: Hava durumu güncellendi!");
        System.out.println("   Sıcaklık: " + temperature + "°C | Nem: " + humidity + "% | Basınç: " + pressure + " hPa");

        // Ekstrem durumlarda özel uyarı
        if (temperature > 35) {
            System.out.println("   ⚠️  UYARI: Aşırı sıcak! Su içmeyi unutma!");
        } else if (temperature < 0) {
            System.out.println("   ⚠️  UYARI: Donma noktasının altında! Sıcak giyinin!");
        }
    }

    /**
     * Kullanıcı bildirimleri kapatabilir
     */
    public void unsubscribe() {
        weatherStation.removeObserver(this);
        System.out.println("📱 " + userName + " bildirimleri kapattı.");
    }
}
