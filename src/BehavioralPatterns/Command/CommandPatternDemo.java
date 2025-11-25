package BehavioralPatterns.Command;

/**
 * COMMAND PATTERN DEMO
 *
 * Akıllı ev sistemi örneği üzerinden Command Pattern'in nasıl çalıştığını gösterir.
 *
 * === PATTERN YAPISI ===
 *
 * 1. Command (Interface): Tüm komutların implement edeceği arayüz
 *    - execute() ve undo() metotları
 *
 * 2. Concrete Commands: Spesifik komutlar
 *    - LightOnCommand, LightOffCommand
 *    - AirConditionerOnCommand, AirConditionerOffCommand
 *    - MacroCommand (birden fazla komutu gruplar)
 *
 * 3. Receiver: İşi gerçekten yapan sınıflar
 *    - Light (ışık)
 *    - AirConditioner (klima)
 *
 * 4. Invoker: Komutları çalıştıran sınıf
 *    - RemoteControl (uzaktan kumanda)
 *    - Hangi komutun ne yaptığını bilmez, sadece execute() çağırır
 *
 * 5. Client: Komutları oluşturup invoker'a set eden
 *    - Bu demo sınıfı
 *
 * === AVANTAJLARI ===
 * ✅ Undo/Redo kolayca implement edilir
 * ✅ İstek gönderen ile işlemi yapan ayrıştırılır (decoupling)
 * ✅ Yeni komutlar eklemek kolay (Open/Closed Principle)
 * ✅ Komutlar compose edilebilir (MacroCommand)
 * ✅ Komutlar loglanabilir, kuyruğa alınabilir
 *
 * === DEZAVANTAJLARI ===
 * ❌ Her işlem için yeni sınıf gerekir
 * ❌ Sınıf sayısı artabilir
 */
public class CommandPatternDemo {

    public static void main(String[] args) {
        System.out.println("🏠 ===== AKILLI EV SİSTEMİ =====\n");

        // RECEIVER'ları oluştur (gerçek işi yapan nesneler)
        Light livingRoomLight = new Light("Oturma Odası");
        Light bedroomLight = new Light("Yatak Odası");
        AirConditioner livingRoomAC = new AirConditioner("Oturma Odası");

        // COMMAND nesnelerini oluştur
        LightOnCommand livingRoomLightOn = new LightOnCommand(livingRoomLight);
        LightOffCommand livingRoomLightOff = new LightOffCommand(livingRoomLight);

        LightOnCommand bedroomLightOn = new LightOnCommand(bedroomLight);
        LightOffCommand bedroomLightOff = new LightOffCommand(bedroomLight);

        AirConditionerOnCommand livingRoomACOn = new AirConditionerOnCommand(livingRoomAC);
        AirConditionerOffCommand livingRoomACOff = new AirConditionerOffCommand(livingRoomAC);

        // INVOKER oluştur (uzaktan kumanda)
        RemoteControl remote = new RemoteControl();

        // Uzaktan kumandaya komutları ata
        remote.setCommand(0, livingRoomLightOn, livingRoomLightOff);
        remote.setCommand(1, bedroomLightOn, bedroomLightOff);
        remote.setCommand(2, livingRoomACOn, livingRoomACOff);

        remote.printStatus();

        // === SENARYO 1: Temel Komut Kullanımı ===
        System.out.println("📍 SENARYO 1: Temel Komutlar\n");
        System.out.println("Slot 0 ON butonuna basıldı:");
        remote.onButtonPressed(0);

        System.out.println("\nSlot 1 ON butonuna basıldı:");
        remote.onButtonPressed(1);

        System.out.println("\nSlot 2 ON butonuna basıldı:");
        remote.onButtonPressed(2);

        System.out.println("\n--- Şimdi kapatıyoruz ---");
        System.out.println("\nSlot 0 OFF butonuna basıldı:");
        remote.offButtonPressed(0);

        // === SENARYO 2: UNDO Kullanımı ===
        System.out.println("\n\n📍 SENARYO 2: UNDO Özelliği\n");
        System.out.println("UNDO butonuna basıldı:");
        remote.undoButtonPressed();  // Oturma odası ışığını tekrar açar

        System.out.println("\nUNDO butonuna basıldı:");
        remote.undoButtonPressed();  // Klimayı kapatır

        System.out.println("\nUNDO butonuna basıldı:");
        remote.undoButtonPressed();  // Yatak odası ışığını kapatır

        // === SENARYO 3: MACRO COMMAND ===
        System.out.println("\n\n📍 SENARYO 3: Makro Komut (Eve Geliyorum Modu)\n");

        // "Eve geliyorum" makro komutu oluştur
        Command[] partyOn = {
            livingRoomLightOn,
            bedroomLightOn,
            livingRoomACOn
        };
        MacroCommand partyOnMacro = new MacroCommand(partyOn);

        Command[] partyOff = {
            livingRoomLightOff,
            bedroomLightOff,
            livingRoomACOff
        };
        MacroCommand partyOffMacro = new MacroCommand(partyOff);

        // Slot 3'e makro komutu ata
        remote.setCommand(3, partyOnMacro, partyOffMacro);

        System.out.println("Slot 3 ON butonuna basıldı (Eve Geliyorum Modu):");
        remote.onButtonPressed(3);

        System.out.println("\nSlot 3 OFF butonuna basıldı (Hepsini Kapat):");
        remote.offButtonPressed(3);

        System.out.println("\nUNDO butonuna basıldı (Makro komutunu geri al):");
        remote.undoButtonPressed();

        System.out.println("\n\n🎓 ===== PATTERN AÇIKLAMASI =====");
        System.out.println("""

                Command Pattern'in Çalışma Mantığı:

                1️⃣ CLIENT (main metodu):
                   - Receiver nesneleri oluşturur (Light, AirConditioner)
                   - Command nesneleri oluşturur ve receiver'ları enjekte eder
                   - Invoker'a (RemoteControl) komutları set eder

                2️⃣ INVOKER (RemoteControl):
                   - Komutları bilmez, sadece execute() çağırır
                   - Butona basıldığında ilgili komutu çalıştırır
                   - Undo için komut geçmişini tutar

                3️⃣ COMMAND (LightOnCommand, vb.):
                   - Receiver'ı tutar
                   - execute() çağrıldığında receiver'ın metodunu çağırır
                   - undo() için ters işlemi bilir

                4️⃣ RECEIVER (Light, AirConditioner):
                   - Gerçek işi yapar
                   - Command pattern'den habersizdir

                💡 Avantaj: Yeni bir cihaz eklemek istersen (TV, Müzik Sistemi vb.):
                   - Receiver sınıfı oluştur
                   - OnCommand ve OffCommand sınıfları oluştur
                   - RemoteControl'e set et
                   - Mevcut kodu değiştirmene gerek yok!
                """);
    }
}
