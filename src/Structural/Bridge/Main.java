package Structural.Bridge;

/**
 * ADIM 5: Demo / Client Code
 *
 * Bridge Pattern'in calismasini gosteren ornek.
 *
 * DIKKAT EDILMESI GEREKENLER:
 * 1. Device'lar bagimsiz olarak olusturuluyor
 * 2. Remote'lar Device'lari constructor'da aliyor (Dependency Injection)
 * 3. Ayni remote tipi farkli cihazlarla calisabiliyor
 * 4. Ayni cihaz farkli remote'larla kontrol edilebilir
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    BRIDGE DESIGN PATTERN DEMO");
        System.out.println("========================================\n");

        // -------------------------------------------------
        // SENARYO 1: BasicRemote ile TV kontrolu
        // -------------------------------------------------
        System.out.println(">>> SENARYO 1: BasicRemote + TV");
        System.out.println("-".repeat(40));

        // Cihazi olustur
        Device tv = new Tv();

        // Remote'u cihaza bagla (KOPRU KURULDU!)
        BasicRemote basicRemote = new BasicRemote(tv);

    // Remote uzerinden cihazi kontrol et
        basicRemote.togglePower();      // TV'yi ac
        basicRemote.volumeUp();         // Sesi artir
        basicRemote.volumeUp();         // Sesi artir
        basicRemote.channelUp();        // Kanal degistir
        basicRemote.printDeviceStatus();

        // -------------------------------------------------
        // SENARYO 2: AdvancedRemote ile Radio kontrolu
        // -------------------------------------------------
        System.out.println(">>> SENARYO 2: AdvancedRemote + Radio");
        System.out.println("-".repeat(40));

        // Farkli bir cihaz olustur
        Device radio = new Radio();

        // Gelismis remote'u radyoya bagla
        AdvancedRemote advancedRemote = new AdvancedRemote(radio);

        advancedRemote.togglePower();   // Radio'yu ac
        advancedRemote.volumeUp();      // Sesi artir
        advancedRemote.volumeUp();      // Sesi artir
        advancedRemote.volumeUp();      // Sesi artir (max 50'de takilacak)
        advancedRemote.printDeviceStatus();

        // Gelismis ozellik: Mute
        advancedRemote.mute();
        advancedRemote.printDeviceStatus();

        // -------------------------------------------------
        // SENARYO 3: Ayni remote, farkli cihaz
        // -------------------------------------------------
        System.out.println(">>> SENARYO 3: AdvancedRemote + TV");
        System.out.println("-".repeat(40));

        // Ayni AdvancedRemote sinifi, bu sefer TV ile
        Device tv2 = new Tv();
        AdvancedRemote advancedRemoteForTv = new AdvancedRemote(tv2);

        advancedRemoteForTv.togglePower();
        advancedRemoteForTv.volumeUp();
        advancedRemoteForTv.volumeUp();
        advancedRemoteForTv.mute();  // TV icin de mute calisiyor!
        advancedRemoteForTv.printDeviceStatus();

        // -------------------------------------------------
        // OZET
        // -------------------------------------------------
        System.out.println("========================================");
        System.out.println("    BRIDGE PATTERN OZETI");
        System.out.println("========================================");
        System.out.println("- RemoteControl (Abstraction) ve Device (Implementation)");
        System.out.println("  birbirinden BAGIMSIZ olarak genisleyebilir.");
        System.out.println("- Yeni cihaz eklemek icin: Device interface'ini implement et");
        System.out.println("- Yeni kumanda eklemek icin: RemoteControl'u extend et");
        System.out.println("- Hicbir mevcut kod degismez!");
        System.out.println("========================================");
    }
}
