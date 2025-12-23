package BehavioralPatterns.State.Document;

// ============================================
// STATE INTERFACE
// ============================================
/**
 * DocumentState: Döküman durumlarının arayüzü
 * Her durum bu metotları kendi mantığına göre implement eder
 */
interface DocumentState {
    void edit(Document doc);
    void submit(Document doc);
    void publish(Document doc);
    void reject(Document doc);
    String getStateName();
}

// ============================================
// CONTEXT (DÖKÜMAN)
// ============================================
/**
 * Document: Durum sahibi nesne
 * - Mevcut durumu tutar
 * - İşlemleri mevcut state'e delege eder
 */
class Document {
    private DocumentState currentState;
    private String content;

    public Document(String content) {
        this.content = content;
        this.currentState = new DraftState();  // Başlangıç durumu
        System.out.println("📄 Döküman oluşturuldu: \"" + content + "\"");
        System.out.println("   Durum: " + currentState.getStateName());
    }

    public void setState(DocumentState state) {
        this.currentState = state;
        System.out.println("   ➡️  Durum değişti: " + state.getStateName());
    }

    public DocumentState getState() {
        return currentState;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // İşlemler - state'e delege edilir
    public void edit() {
        System.out.println("\n🖊️  Düzenleme isteği...");
        currentState.edit(this);
    }

    public void submit() {
        System.out.println("\n📤 Gönderim isteği...");
        currentState.submit(this);
    }

    public void publish() {
        System.out.println("\n🚀 Yayınlama isteği...");
        currentState.publish(this);
    }

    public void reject() {
        System.out.println("\n❌ Reddetme isteği...");
        currentState.reject(this);
    }
}

// ============================================
// CONCRETE STATES
// ============================================

/**
 * DraftState: Taslak durumu
 * - Düzenlenebilir
 * - İncelemeye gönderilebilir
 * - Yayınlanamaz
 */
class DraftState implements DocumentState {

    @Override
    public void edit(Document doc) {
        System.out.println("   ✅ Döküman düzenlendi");
        // Durum değişmez, hala Draft
    }

    @Override
    public void submit(Document doc) {
        System.out.println("   ✅ Döküman incelemeye gönderildi");
        doc.setState(new ReviewState());
    }

    @Override
    public void publish(Document doc) {
        System.out.println("   ❌ Hata: Taslak döküman direkt yayınlanamaz!");
        System.out.println("   💡 Önce incelemeye gönderin");
    }

    @Override
    public void reject(Document doc) {
        System.out.println("   ⚠️  Döküman zaten taslak durumunda");
    }

    @Override
    public String getStateName() {
        return "DRAFT (Taslak)";
    }
}

/**
 * ReviewState: İnceleme durumu
 * - Düzenlenemez
 * - Yayınlanabilir
 * - Reddedilebilir (Draft'a döner)
 */
class ReviewState implements DocumentState {

    @Override
    public void edit(Document doc) {
        System.out.println("   ❌ Hata: İncelemede olan döküman düzenlenemez!");
        System.out.println("   💡 Önce reddedin, sonra düzenleyin");
    }

    @Override
    public void submit(Document doc) {
        System.out.println("   ⚠️  Döküman zaten incelemede");
    }

    @Override
    public void publish(Document doc) {
        System.out.println("   ✅ Döküman yayınlandı!");
        doc.setState(new PublishedState());
    }

    @Override
    public void reject(Document doc) {
        System.out.println("   ✅ Döküman reddedildi, taslağa geri döndü");
        doc.setState(new DraftState());
    }

    @Override
    public String getStateName() {
        return "REVIEW (İncelemede)";
    }
}

/**
 * PublishedState: Yayınlanmış durumu
 * - Düzenlenemez
 * - Gönderilemez
 * - Tekrar yayınlanamaz
 */
class PublishedState implements DocumentState {

    @Override
    public void edit(Document doc) {
        System.out.println("   ❌ Hata: Yayınlanmış döküman düzenlenemez!");
        System.out.println("   💡 Yeni bir taslak oluşturun");
    }

    @Override
    public void submit(Document doc) {
        System.out.println("   ⚠️  Döküman zaten yayınlanmış");
    }

    @Override
    public void publish(Document doc) {
        System.out.println("   ⚠️  Döküman zaten yayınlanmış durumda");
    }

    @Override
    public void reject(Document doc) {
        System.out.println("   ❌ Hata: Yayınlanmış döküman reddedilemez!");
    }

    @Override
    public String getStateName() {
        return "PUBLISHED (Yayınlanmış)";
    }
}

// ============================================
// DEMO - STATE PATTERN
// ============================================
/**
 * AMAÇ: Nesnenin durumuna göre davranışını değiştirmek
 *
 * SENARYO: Döküman Yaşam Döngüsü
 * - Draft → Review → Published
 * - Her durumda farklı işlemler yapılabilir
 *
 * STATE OLMADAN:
 * class Document {
 *     String status;
 *
 *     void edit() {
 *         if (status.equals("draft")) {
 *             // düzenle
 *         } else if (status.equals("review")) {
 *             // hata
 *         } else if (status.equals("published")) {
 *             // hata
 *         }
 *     }
 *     // Her metod için aynı if-else tekrarı!
 * }
 *
 * STATE İLE:
 * class Document {
 *     DocumentState state;
 *
 *     void edit() {
 *         state.edit(this);  // State karar verir
 *     }
 * }
 *
 * FAYDA:
 * - Her durum ayrı sınıf (Single Responsibility)
 * - If-else karmaşası yok
 * - Yeni durum eklemek kolay
 * - State geçişleri açık ve net
 */
public class DocumentStateDemo {
    public static void main(String[] args) {
        System.out.println("🎯 STATE PATTERN - DÖKÜMAN DURUM YÖNETİMİ\n");

        // Döküman oluştur (başlangıçta Draft)
        Document doc = new Document("Design Patterns Rehberi");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 SENARYO 1: Normal İş Akışı");
        System.out.println("=".repeat(60));

        // Draft durumunda düzenle
        doc.edit();

        // İncelemeye gönder
        doc.submit();

        // Yayınla
        doc.publish();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 SENARYO 2: Hatalı İşlemler");
        System.out.println("=".repeat(60));

        // Yayınlanmış dökümanı düzenlemeye çalış
        doc.edit();

        // Yeni döküman
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 SENARYO 3: Reddetme İşlemi");
        System.out.println("=".repeat(60));

        Document doc2 = new Document("Yeni Makale");
        doc2.edit();
        doc2.submit();

        // İncelemeden reddet
        doc2.reject();

        // Tekrar düzenle ve gönder
        doc2.edit();
        doc2.submit();
        doc2.publish();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 DURUM GEÇİŞ DİYAGRAMI:");
        System.out.println("=".repeat(60));
        System.out.println("        submit()          publish()");
        System.out.println("  DRAFT ---------> REVIEW ---------> PUBLISHED");
        System.out.println("    ↑                |");
        System.out.println("    |    reject()    |");
        System.out.println("    +----------------+");
        System.out.println("=".repeat(60));

        System.out.println("\n" + "=".repeat(60));
        System.out.println("💡 STATE PATTERN'IN FAYDASI:");
        System.out.println("=".repeat(60));
        System.out.println("✅ Her durum ayrı sınıf (Single Responsibility)");
        System.out.println("✅ If-else karmaşası yok");
        System.out.println("✅ Yeni durum eklemek kolay");
        System.out.println("✅ State geçişleri açık ve net");
        System.out.println("✅ Duruma göre farklı davranış");
        System.out.println("=".repeat(60));
    }
}
