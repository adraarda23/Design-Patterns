package BehavioralPatterns.ChainOfResponsibility.SupportTicket;

// ============================================
// REQUEST (İSTEK)
// ============================================
/**
 * SupportTicket: Destek talebi
 * Öncelik seviyesi ve açıklama içerir
 */
class SupportTicket {
    private String description;
    private Priority priority;

    public enum Priority {
        LOW(1),      // Basit sorular
        MEDIUM(2),   // Teknik sorunlar
        HIGH(3),     // Karmaşık sorunlar
        CRITICAL(4); // Kritik sorunlar

        private final int level;

        Priority(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    public SupportTicket(String description, Priority priority) {
        this.description = description;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }
}

// ============================================
// HANDLER (BASE CLASS)
// ============================================
/**
 * SupportHandler: Destek zincirinin base sınıfı
 * Her handler bir sonraki handler'ı bilir
 */
abstract class SupportHandler {
    protected SupportHandler nextHandler;
    protected int handlerLevel;

    public SupportHandler(int level) {
        this.handlerLevel = level;
    }

    // Zincirdeki bir sonraki handler'ı ayarla
    public void setNext(SupportHandler next) {
        this.nextHandler = next;
    }

    // İsteği işle
    public void handleRequest(SupportTicket ticket) {
        if (ticket.getPriority().getLevel() <= handlerLevel) {
            // Bu handler işleyebilir
            process(ticket);
        } else if (nextHandler != null) {
            // Bir sonraki handler'a ilet
            System.out.println("   ↓ Üst seviyeye yönlendiriliyor...");
            nextHandler.handleRequest(ticket);
        } else {
            // Zincir sonu, kimse işleyemedi
            System.out.println("❌ Bu talep işlenemiyor!");
        }
    }

    // Alt sınıflar bu metodu implement edecek
    protected abstract void process(SupportTicket ticket);
}

// ============================================
// CONCRETE HANDLERS
// ============================================

/**
 * Level1Support: Seviye 1 destek
 * Basit soruları çözer (şifre sıfırlama, genel sorular)
 */
class Level1Support extends SupportHandler {

    public Level1Support() {
        super(1);  // Sadece LOW priority işler
    }

    @Override
    protected void process(SupportTicket ticket) {
        System.out.println("👤 Level 1 Support: \"" + ticket.getDescription() + "\" çözüldü");
        System.out.println("   → Basit sorun, hemen çözüldü!");
    }
}

/**
 * Level2Support: Seviye 2 destek
 * Teknik sorunları çözer
 */
class Level2Support extends SupportHandler {

    public Level2Support() {
        super(2);  // LOW ve MEDIUM priority işler
    }

    @Override
    protected void process(SupportTicket ticket) {
        System.out.println("🔧 Level 2 Support: \"" + ticket.getDescription() + "\" çözüldü");
        System.out.println("   → Teknik sorun, uzman ekip çözdü");
    }
}

/**
 * Level3Support: Seviye 3 destek
 * Karmaşık sorunları çözer
 */
class Level3Support extends SupportHandler {

    public Level3Support() {
        super(3);  // LOW, MEDIUM ve HIGH priority işler
    }

    @Override
    protected void process(SupportTicket ticket) {
        System.out.println("⚙️  Level 3 Support: \"" + ticket.getDescription() + "\" çözüldü");
        System.out.println("   → Karmaşık sorun, senior ekip çözdü");
    }
}

/**
 * Manager: Yönetici
 * Kritik sorunları çözer
 */
class Manager extends SupportHandler {

    public Manager() {
        super(4);  // Tüm priority'leri işler
    }

    @Override
    protected void process(SupportTicket ticket) {
        System.out.println("👔 Manager: \"" + ticket.getDescription() + "\" çözüldü");
        System.out.println("   → Kritik sorun, yönetici müdahale etti!");
    }
}

// ============================================
// DEMO - CHAIN OF RESPONSIBILITY
// ============================================
/**
 * AMAÇ: İstekleri zincir boyunca ileterek uygun handler'ın işlemesini sağlamak
 *
 * SENARYO: Müşteri Destek Sistemi
 * - Farklı öncelik seviyeleri (LOW, MEDIUM, HIGH, CRITICAL)
 * - Her seviye farklı destek ekibi tarafından işlenir
 * - İstek uygun seviyeye kadar ilerler
 *
 * CHAIN OLMADAN:
 * if (priority == LOW) {
 *     level1.handle();
 * } else if (priority == MEDIUM) {
 *     level2.handle();
 * } else if (priority == HIGH) {
 *     level3.handle();
 * } else {
 *     manager.handle();
 * }
 * // Sıkı bağlantı, esneklik yok
 *
 * CHAIN İLE:
 * chain.handleRequest(ticket);
 * // Handler'lar birbirini bilmiyor
 * // Zinciri runtime'da değiştirebilirsin
 *
 * FAYDA:
 * - Loose coupling (handler'lar bağımsız)
 * - Flexible (zinciri kolayca değiştir)
 * - Single Responsibility (her handler bir şey yapar)
 */
public class SupportTicketDemo {
    public static void main(String[] args) {
        System.out.println("🎯 CHAIN OF RESPONSIBILITY - DESTEK SİSTEMİ ÖRNEĞİ\n");

        // Zinciri oluştur: Level1 → Level2 → Level3 → Manager
        SupportHandler level1 = new Level1Support();
        SupportHandler level2 = new Level2Support();
        SupportHandler level3 = new Level3Support();
        SupportHandler manager = new Manager();

        level1.setNext(level2);
        level2.setNext(level3);
        level3.setNext(manager);

        // Test talepleri
        SupportTicket[] tickets = {
            new SupportTicket("Şifremi unuttum", SupportTicket.Priority.LOW),
            new SupportTicket("Uygulama çöküyor", SupportTicket.Priority.MEDIUM),
            new SupportTicket("Veritabanı bağlantı hatası", SupportTicket.Priority.HIGH),
            new SupportTicket("Sistem tamamen çalışmıyor!", SupportTicket.Priority.CRITICAL)
        };

        // Her talebi zincire gönder
        for (int i = 0; i < tickets.length; i++) {
            System.out.println("=".repeat(60));
            System.out.println("📋 Talep " + (i + 1) + ": " + tickets[i].getDescription());
            System.out.println("   Öncelik: " + tickets[i].getPriority());
            System.out.println("=".repeat(60));

            level1.handleRequest(tickets[i]);  // Zincirin başından başla

            System.out.println();
        }

        // Açıklama
        System.out.println("=".repeat(60));
        System.out.println("💡 CHAIN OF RESPONSIBILITY'NİN FAYDASI:");
        System.out.println("=".repeat(60));
        System.out.println("✅ Her handler kendi seviyesini biliyor");
        System.out.println("✅ İşleyemezse bir sonrakine iletiyor");
        System.out.println("✅ Handler'lar birbirini bilmiyor (loose coupling)");
        System.out.println("✅ Zinciri runtime'da değiştirebilirsin");
        System.out.println("✅ Yeni seviye eklemek kolay");
        System.out.println("=".repeat(60));
    }
}
