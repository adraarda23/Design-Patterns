package BehavioralPatterns.Memento.Incremental;

import java.util.*;

// ============================================
// DEĞİŞİKLİK KAYDI (Delta/Diff)
// ============================================
/**
 * TextChange: Bir değişikliği temsil eder (Git commit gibi)
 * - Ne eklendi? Ne silindi? Nerede?
 *
 * GIT'TEKİ KARŞILIĞI:
 * Git'te her commit benzer şekilde çalışır:
 * - Değişiklik tipi (add/delete/modify)
 * - Değişiklik yeri (line number)
 * - Değişen içerik (diff)
 */
class TextChange {
    enum ChangeType { INSERT, DELETE, REPLACE }

    private final ChangeType type;
    private final int position;      // Değişiklik nerede oldu?
    private final String oldText;    // Eski metin (DELETE/REPLACE için)
    private final String newText;    // Yeni metin (INSERT/REPLACE için)
    private final long timestamp;

    // INSERT constructor
    public static TextChange insert(int position, String text) {
        return new TextChange(ChangeType.INSERT, position, "", text);
    }

    // DELETE constructor
    public static TextChange delete(int position, String text) {
        return new TextChange(ChangeType.DELETE, position, text, "");
    }

    // REPLACE constructor
    public static TextChange replace(int position, String oldText, String newText) {
        return new TextChange(ChangeType.REPLACE, position, oldText, newText);
    }

    private TextChange(ChangeType type, int position, String oldText, String newText) {
        this.type = type;
        this.position = position;
        this.oldText = oldText;
        this.newText = newText;
        this.timestamp = System.currentTimeMillis();
    }

    // Bu değişikliği uygula (apply patch)
    public String apply(String content) {
        switch (type) {
            case INSERT:
                return content.substring(0, position) + newText + content.substring(position);
            case DELETE:
                return content.substring(0, position) +
                       content.substring(position + oldText.length());
            case REPLACE:
                return content.substring(0, position) + newText +
                       content.substring(position + oldText.length());
            default:
                return content;
        }
    }

    // Bu değişikliği geri al (undo/reverse patch)
    public String undo(String content) {
        switch (type) {
            case INSERT:
                // Eklenenı sil
                return content.substring(0, position) +
                       content.substring(position + newText.length());
            case DELETE:
                // Silineni geri ekle
                return content.substring(0, position) + oldText + content.substring(position);
            case REPLACE:
                // Yeniyi sil, eskiyi ekle
                return content.substring(0, position) + oldText +
                       content.substring(position + newText.length());
            default:
                return content;
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] pos=%d, old='%s', new='%s'",
                           type, position, oldText, newText);
    }

    // Memory hesaplama (debugging için)
    public int getMemoryUsage() {
        return oldText.length() + newText.length() + 24; // 24 = overhead (type, position, timestamp)
    }
}

// ============================================
// INCREMENTAL MEMENTO (Delta tabanlı)
// ============================================
/**
 * IncrementalMemento: Sadece değişiklikleri saklar
 * - İlk snapshot (base) + sonraki değişiklikler (deltas)
 *
 * GIT ANALOJİSİ:
 * Git'teki commit chain gibi:
 * Base (initial commit) → Delta 1 → Delta 2 → Delta 3 → ...
 *
 * RESTORE NASIL ÇALIŞIR:
 * Base'den başla, tüm delta'ları sırayla uygula
 * (Git'te: checkout = base + apply all commits until HEAD)
 */
class IncrementalMemento {
    private final String baseContent;              // İlk tam snapshot
    private final List<TextChange> changes;        // Sonraki değişiklikler

    // İlk snapshot için
    public IncrementalMemento(String baseContent) {
        this.baseContent = baseContent;
        this.changes = new ArrayList<>();
    }

    // Değişiklik ekle (git commit benzeri)
    public void addChange(TextChange change) {
        changes.add(change);
    }

    // Tüm değişiklikleri uygulayarak son durumu al (git checkout benzeri)
    public String restore() {
        String content = baseContent;
        for (TextChange change : changes) {
            content = change.apply(content);
        }
        return content;
    }

    // Memory kullanımını hesapla
    public int getMemoryUsage() {
        int total = baseContent.length();
        for (TextChange change : changes) {
            total += change.getMemoryUsage();
        }
        return total;
    }

    public String getBaseContent() {
        return baseContent;
    }

    public List<TextChange> getChanges() {
        return new ArrayList<>(changes);
    }
}

// ============================================
// FULL SNAPSHOT MEMENTO (Karşılaştırma için)
// ============================================
class FullSnapshotMemento {
    private final String content;

    public FullSnapshotMemento(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getMemoryUsage() {
        return content.length();
    }
}

// ============================================
// TEXT EDITOR (Delta desteği ile)
// ============================================
class IncrementalTextEditor {
    private String content;

    public IncrementalTextEditor() {
        this.content = "";
    }

    public void setText(String text) {
        this.content = text;
    }

    public void insert(int position, String text) {
        content = content.substring(0, position) + text + content.substring(position);
    }

    public void delete(int position, int length) {
        content = content.substring(0, position) + content.substring(position + length);
    }

    public String getContent() {
        return content;
    }

    // İlk snapshot'ı oluştur
    public IncrementalMemento createSnapshot() {
        return new IncrementalMemento(content);
    }

    // Full snapshot oluştur (karşılaştırma için)
    public FullSnapshotMemento saveFullSnapshot() {
        return new FullSnapshotMemento(content);
    }

    // Snapshot'tan geri yükle
    public void restore(IncrementalMemento memento) {
        this.content = memento.restore();
    }

    public void restore(FullSnapshotMemento memento) {
        this.content = memento.getContent();
    }
}

// ============================================
// HISTORY MANAGER (Her iki yaklaşımı da destekler)
// ============================================
class SmartHistory {
    private IncrementalMemento incrementalMemento;
    private final List<TextChange> changeLog;
    private final List<FullSnapshotMemento> fullSnapshots;

    public SmartHistory() {
        this.changeLog = new ArrayList<>();
        this.fullSnapshots = new ArrayList<>();
    }

    // Incremental yaklaşım: İlk snapshot'ı kaydet
    public void saveBase(IncrementalMemento memento) {
        this.incrementalMemento = memento;
    }

    // Incremental yaklaşım: Değişiklik ekle
    public void recordChange(TextChange change) {
        if (incrementalMemento != null) {
            incrementalMemento.addChange(change);
        }
        changeLog.add(change);
    }

    // Full snapshot yaklaşım
    public void saveFullSnapshot(FullSnapshotMemento memento) {
        fullSnapshots.add(memento);
    }

    // Memory istatistikleri
    public void printMemoryStats() {
        System.out.println("\n📊 MEMORY KULLANIM İSTATİSTİKLERİ:");
        System.out.println("=".repeat(60));

        // Incremental memory
        if (incrementalMemento != null) {
            int incrementalMemory = incrementalMemento.getMemoryUsage();
            System.out.println("🔹 Incremental (Delta) Yaklaşım:");
            System.out.println("   Base: " + incrementalMemento.getBaseContent().length() + " byte");
            System.out.println("   Changes: " + changeLog.size() + " adet");
            System.out.println("   Toplam: " + incrementalMemory + " byte");
        }

        // Full snapshot memory
        if (!fullSnapshots.isEmpty()) {
            int fullMemory = 0;
            for (FullSnapshotMemento m : fullSnapshots) {
                fullMemory += m.getMemoryUsage();
            }
            System.out.println("\n🔹 Full Snapshot Yaklaşım:");
            System.out.println("   Snapshot sayısı: " + fullSnapshots.size());
            System.out.println("   Toplam: " + fullMemory + " byte");

            // Karşılaştırma
            if (incrementalMemento != null) {
                int incrementalMemory = incrementalMemento.getMemoryUsage();
                double savings = ((double)(fullMemory - incrementalMemory) / fullMemory) * 100;
                System.out.println("\n💡 TASARRUF:");
                System.out.println("   Delta yaklaşımı: %" + String.format("%.1f", savings) + " daha az bellek!");
            }
        }
        System.out.println("=".repeat(60));
    }

    public IncrementalMemento getIncrementalMemento() {
        return incrementalMemento;
    }
}

// ============================================
// DEMO - INCREMENTAL MEMENTO PATTERN
// ============================================
/**
 * YAKLAŞIM: Incremental/Delta (Git-like)
 *
 * AVANTAJLAR:
 * - Büyük dosyalarda bellek tasarrufu (%90'a kadar!)
 * - Git gibi değişiklik geçmişi
 * - Sadece farkları saklar
 *
 * DEZAVANTAJLAR:
 * - Restore daha yavaş (tüm delta'lar uygulanmalı)
 * - Daha karmaşık implementasyon
 * - Küçük dosyalarda overhead yüksek olabilir
 *
 * NE ZAMAN KULLANILMALI:
 * - Büyük state boyutları (> 10KB)
 * - Çok sayıda snapshot (> 50)
 * - Benzer içerikler (kod dosyaları, dökümanlar)
 * - Bellek kritik uygulamalar (mobile, embedded)
 */
public class IncrementalMementoDemo {
    public static void main(String[] args) {
        System.out.println("🎯 INCREMENTAL MEMENTO vs FULL SNAPSHOT KARŞILAŞTIRMASI\n");
        System.out.println("💡 Git gibi çalışan bir Memento Pattern implementasyonu\n");

        IncrementalTextEditor editor = new IncrementalTextEditor();
        SmartHistory history = new SmartHistory();

        // İlk durum
        editor.setText("Merhaba");
        System.out.println("1️⃣  İlk durum: \"" + editor.getContent() + "\"");

        // İlk snapshot'ı kaydet (her iki yaklaşım için de)
        history.saveBase(editor.createSnapshot());
        history.saveFullSnapshot(editor.saveFullSnapshot());

        // Değişiklik 1: Ekleme
        System.out.println("\n2️⃣  Değişiklik: ' Dünya' ekleniyor...");
        int pos = editor.getContent().length();
        editor.insert(pos, " Dünya");

        // Kayıt
        TextChange change1 = TextChange.insert(pos, " Dünya");
        history.recordChange(change1);
        history.saveFullSnapshot(editor.saveFullSnapshot());
        System.out.println("    Yeni durum: \"" + editor.getContent() + "\"");

        // Değişiklik 2: Ekleme
        System.out.println("\n3️⃣  Değişiklik: '!' ekleniyor...");
        pos = editor.getContent().length();
        editor.insert(pos, "!");

        // Kayıt
        TextChange change2 = TextChange.insert(pos, "!");
        history.recordChange(change2);
        history.saveFullSnapshot(editor.saveFullSnapshot());
        System.out.println("    Yeni durum: \"" + editor.getContent() + "\"");

        // Değişiklik 3: Uzun bir ekleme
        System.out.println("\n4️⃣  Değişiklik: ' Nasılsın? Ben iyiyim.' ekleniyor...");
        pos = editor.getContent().length();
        String longText = " Nasılsın? Ben iyiyim.";
        editor.insert(pos, longText);

        // Kayıt
        TextChange change3 = TextChange.insert(pos, longText);
        history.recordChange(change3);
        history.saveFullSnapshot(editor.saveFullSnapshot());
        System.out.println("    Yeni durum: \"" + editor.getContent() + "\"");

        // Memory istatistikleri
        history.printMemoryStats();

        // Bonus: Change log göster (git log benzeri)
        System.out.println("\n📝 DEĞİŞİKLİK KAYDI (git log benzeri):");
        System.out.println("=".repeat(60));
        List<TextChange> changes = history.getIncrementalMemento().getChanges();
        for (int i = 0; i < changes.size(); i++) {
            System.out.println("Commit " + (i+1) + ": " + changes.get(i));
        }

        System.out.println("\n✅ Küçük dosyalarda overhead var ama büyük dosyalarda muazzam tasarruf!");
    }
}
