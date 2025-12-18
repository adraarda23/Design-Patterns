package BehavioralPatterns.Observer;

/**
 * Observer Pattern Demo
 * Hava durumu istasyonu örneği ile Observer pattern'in çalışmasını gösterir
 */
public class WeatherStationDemo {
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🌤️  OBSERVER DESIGN PATTERN - HAVA DURUMU İSTASYONU ÖRNEĞİ");
        System.out.println("=".repeat(70));

        // Subject (Özne) oluştur
        WeatherStation weatherStation = new WeatherStation();

        System.out.println("\n📡 Hava durumu istasyonu başlatılıyor...\n");

        // Observer'ları (Gözlemcileri) oluştur ve kaydet
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherStation);
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherStation);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherStation);
        MobileApp ahmetApp = new MobileApp("Ahmet", weatherStation);
        MobileApp ayseApp = new MobileApp("Ayşe", weatherStation);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("📍 SENARYO 1: İlk hava durumu verisi geldi");
        System.out.println("=".repeat(70));

        // İlk hava durumu verisi - Tüm observer'lar bilgilendirilecek
        weatherStation.setMeasurements(28.5f, 65.0f, 1013.2f);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("📍 SENARYO 2: Hava ısındı ve nem arttı");
        System.out.println("=".repeat(70));

        // Hava durumu değişti
        weatherStation.setMeasurements(32.0f, 75.0f, 1012.5f);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("📍 SENARYO 3: Ahmet bildirimleri kapattı");
        System.out.println("=".repeat(70));

        // Ahmet artık bildirim almak istemiyor
        ahmetApp.unsubscribe();

        System.out.println("\n" + "=".repeat(70));
        System.out.println("📍 SENARYO 4: Soğuk ve yağmurlu bir gün");
        System.out.println("=".repeat(70));

        // Yeni hava durumu - Ahmet bildirim almayacak
        weatherStation.setMeasurements(-2.0f, 90.0f, 1008.0f);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("📍 SENARYO 5: Yeni bir mobil uygulama kullanıcısı eklendi");
        System.out.println("=".repeat(70));

        // Runtime'da yeni observer eklenebilir
        MobileApp mehmetApp = new MobileApp("Mehmet", weatherStation);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("📍 SENARYO 6: Çok sıcak bir yaz günü");
        System.out.println("=".repeat(70));

        weatherStation.setMeasurements(38.0f, 45.0f, 1015.0f);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("✅ OBSERVER PATTERN ÖZETİ");
        System.out.println("=".repeat(70));
        System.out.println("✓ Subject (WeatherStation): Hava durumu verilerini tutar");
        System.out.println("✓ Observer'lar: Ekranlar ve mobil uygulamalar");
        System.out.println("✓ Durum değiştiğinde tüm observer'lar otomatik bilgilendirildi");
        System.out.println("✓ Runtime'da observer ekleyip çıkarabildik");
        System.out.println("✓ Observer'lar birbirinden ve Subject'ten bağımsız (Loose Coupling)");
        System.out.println("=".repeat(70));
    }
}
