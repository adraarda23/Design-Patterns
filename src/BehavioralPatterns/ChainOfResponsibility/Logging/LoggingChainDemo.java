package BehavioralPatterns.ChainOfResponsibility.Logging;

// ============================================
// LOG SEVİYELERİ
// ============================================
enum LogLevel {
    DEBUG(1),
    INFO(2),
    WARNING(3),
    ERROR(4);

    private final int level;

    LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

// ============================================
// HANDLER (BASE CLASS)
// ============================================
/**
 * Logger: Log zincirinin base sınıfı
 *
 * BU BAŞKA BİR CHAIN TİPİ: Responsibility Sharing
 * - Birden fazla handler işlem yapabilir
 * - Zincir durmaz, tüm handler'lar çalışır
 * - Her handler kendi seviyesine uygunsa işler
 */
abstract class Logger {
    protected LogLevel logLevel;
    protected Logger nextLogger;

    public Logger(LogLevel level) {
        this.logLevel = level;
    }

    public void setNext(Logger next) {
        this.nextLogger = next;
    }

    // Log mesajını işle
    public void log(LogLevel level, String message) {
        // Bu logger bu seviyeyi işleyebilir mi?
        if (level.getLevel() >= this.logLevel.getLevel()) {
            write(level, message);
        }

        // Zincir devam eder (durmaz!)
        if (nextLogger != null) {
            nextLogger.log(level, message);
        }
    }

    // Alt sınıflar bu metodu implement edecek
    protected abstract void write(LogLevel level, String message);
}

// ============================================
// CONCRETE LOGGERS
// ============================================

/**
 * ConsoleLogger: Konsola log yazar
 * Tüm seviyeleri konsola yazar
 */
class ConsoleLogger extends Logger {

    public ConsoleLogger() {
        super(LogLevel.DEBUG);  // Tüm seviyeleri kabul et
    }

    @Override
    protected void write(LogLevel level, String message) {
        System.out.println("🖥️  [CONSOLE] " + level + ": " + message);
    }
}

/**
 * FileLogger: Dosyaya log yazar
 * INFO ve üzeri seviyeleri dosyaya yazar
 */
class FileLogger extends Logger {

    public FileLogger() {
        super(LogLevel.INFO);  // INFO ve üzeri
    }

    @Override
    protected void write(LogLevel level, String message) {
        System.out.println("📄 [FILE] " + level + ": " + message + " (dosyaya yazıldı)");
    }
}

/**
 * EmailLogger: Email gönderir
 * ERROR seviyesinde email gönderir
 */
class EmailLogger extends Logger {

    public EmailLogger() {
        super(LogLevel.ERROR);  // Sadece ERROR
    }

    @Override
    protected void write(LogLevel level, String message) {
        System.out.println("📧 [EMAIL] " + level + ": " + message + " (email gönderildi!)");
    }
}

/**
 * DatabaseLogger: Veritabanına log yazar
 * WARNING ve üzeri seviyeleri veritabanına yazar
 */
class DatabaseLogger extends Logger {

    public DatabaseLogger() {
        super(LogLevel.WARNING);  // WARNING ve üzeri
    }

    @Override
    protected void write(LogLevel level, String message) {
        System.out.println("💾 [DATABASE] " + level + ": " + message + " (DB'ye kaydedildi)");
    }
}

// ============================================
// DEMO - LOGGING CHAIN (RESPONSIBILITY SHARING)
// ============================================
/**
 * AMAÇ: Responsibility Sharing - Birden fazla handler işlem yapar
 *
 * SENARYO: Loglama Sistemi
 * - Farklı log seviyeleri (DEBUG, INFO, WARNING, ERROR)
 * - Farklı çıktılar (Console, File, Email, Database)
 * - Seviyeye göre farklı kombinasyonlar
 *
 * LOG SEVİYE KURALLARI:
 * DEBUG   → Sadece Console
 * INFO    → Console + File
 * WARNING → Console + File + Database
 * ERROR   → Console + File + Database + Email
 *
 * ÜÇÜNCÜ CHAIN TİPİ:
 *
 * 1. KLASİK CHAIN (Support Ticket):
 *    - Sadece BİR handler işler
 *    - İşleyemeyen iletir
 *
 * 2. FILTERING CHAIN (Authentication):
 *    - TÜM handler'lar kontrol yapar
 *    - Biri başarısız olursa DURUR
 *
 * 3. RESPONSIBILITY SHARING (Logging):
 *    - BİRDEN FAZLA handler işlem yapar
 *    - Zincir DURMAZ
 *    - Her handler kendi seviyesine uygunsa işler
 *
 * FAYDA:
 * - Seviye bazlı farklı log çıktıları
 * - Yeni logger eklemek kolay
 * - Logger'lar birbirini bilmiyor
 * - Runtime'da zinciri değiştirebilirsin
 */
public class LoggingChainDemo {
    public static void main(String[] args) {
        System.out.println("🎯 CHAIN OF RESPONSIBILITY - LOGLAMA SİSTEMİ ÖRNEĞİ\n");
        System.out.println("💡 Bu bir 'Responsibility Sharing' örneğidir");
        System.out.println("   Birden fazla handler işlem yapar, zincir durmaz\n");

        // Zinciri oluştur: Console → File → Database → Email
        Logger console = new ConsoleLogger();
        Logger file = new FileLogger();
        Logger database = new DatabaseLogger();
        Logger email = new EmailLogger();

        console.setNext(file);
        file.setNext(database);
        database.setNext(email);

        // Test 1: DEBUG seviyesi
        System.out.println("=".repeat(60));
        System.out.println("📋 TEST 1: DEBUG Seviyesi");
        System.out.println("   Beklenen: Sadece Console");
        System.out.println("=".repeat(60));
        console.log(LogLevel.DEBUG, "Değişken değeri: x = 42");

        // Test 2: INFO seviyesi
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 TEST 2: INFO Seviyesi");
        System.out.println("   Beklenen: Console + File");
        System.out.println("=".repeat(60));
        console.log(LogLevel.INFO, "Kullanıcı giriş yaptı: user123");

        // Test 3: WARNING seviyesi
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 TEST 3: WARNING Seviyesi");
        System.out.println("   Beklenen: Console + File + Database");
        System.out.println("=".repeat(60));
        console.log(LogLevel.WARNING, "Disk alanı azalıyor: %15 kaldı");

        // Test 4: ERROR seviyesi
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 TEST 4: ERROR Seviyesi");
        System.out.println("   Beklenen: Console + File + Database + Email");
        System.out.println("=".repeat(60));
        console.log(LogLevel.ERROR, "Veritabanı bağlantısı kesildi!");

        // Özet tablo
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 LOG SEVİYE TABLOSU:");
        System.out.println("=".repeat(60));
        System.out.println("Seviye   | Console | File | Database | Email");
        System.out.println("-".repeat(60));
        System.out.println("DEBUG    |    ✓    |      |          |      ");
        System.out.println("INFO     |    ✓    |  ✓   |          |      ");
        System.out.println("WARNING  |    ✓    |  ✓   |    ✓     |      ");
        System.out.println("ERROR    |    ✓    |  ✓   |    ✓     |  ✓   ");
        System.out.println("=".repeat(60));

        // Açıklama
        System.out.println("\n" + "=".repeat(60));
        System.out.println("💡 RESPONSIBILITY SHARING'İN ÖZELLİKLERİ:");
        System.out.println("=".repeat(60));
        System.out.println("✅ Birden fazla handler işlem yapar");
        System.out.println("✅ Zincir durmaz, tüm handler'lar kontrol edilir");
        System.out.println("✅ Her handler kendi seviyesine bakar");
        System.out.println("✅ Seviyeye göre farklı kombinasyonlar oluşur");
        System.out.println("✅ Yeni logger eklemek kolay");
        System.out.println("=".repeat(60));
    }
}
