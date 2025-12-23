package BehavioralPatterns.Memento.Realistic;

import java.util.*;

/**
 * GERÇEKÇİ SENARYO: Büyük dosya, küçük değişiklikler
 *
 * SENARYO:
 * - 5000 satırlık kod dosyası (~490 KB)
 * - Her değişiklikte sadece 1-2 satır değişiyor
 * - 10 farklı commit
 *
 * AMAÇ: Delta yaklaşımının gerçek gücünü göstermek
 *
 * SONUÇ BEKLENTİSİ:
 * - Full Snapshot: ~5.4 MB (her commit 490 KB)
 * - Delta: ~491 KB (base + küçük değişiklikler)
 * - Tasarruf: %90+
 */

class RealisticScenario {

    // Büyük bir kod dosyası simüle et (gerçek bir Java projesini simüle eder)
    private static String generateLargeCode(int lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines; i++) {
            sb.append("public class Line").append(i).append(" {\n");
            sb.append("    private int value = ").append(i).append(";\n");
            sb.append("    public int getValue() { return value; }\n");
            sb.append("}\n\n");
        }
        return sb.toString();
    }

    // Basit Memento (Full Snapshot)
    static class FullMemento {
        private final String content;

        public FullMemento(String content) {
            this.content = content;
        }

        public int size() {
            return content.length();
        }
    }

    // Delta Memento (Incremental)
    static class DeltaMemento {
        private String baseContent;
        private final List<String> deltas = new ArrayList<>();

        public void setBase(String content) {
            this.baseContent = content;
        }

        public void addDelta(String delta) {
            deltas.add(delta);
        }

        public int size() {
            int total = baseContent.length();
            for (String delta : deltas) {
                total += delta.length() + 16; // +16 overhead (position, type, etc.)
            }
            return total;
        }
    }

    public static void main(String[] args) {
        System.out.println("🎯 GERÇEKÇİ SENARYO: 5000 Satırlık Kod Dosyası\n");
        System.out.println("📌 Bu örnek Git'in neden delta compression kullandığını gösterir\n");

        // İlk kod dosyası (çok büyük - tipik bir Java class dosyası)
        String largeCode = generateLargeCode(5000);
        System.out.println("📄 İlk dosya boyutu: " + largeCode.length() + " byte (~" +
                         (largeCode.length()/1024) + " KB)");

        // FULL SNAPSHOT YAKLAŞIMI
        List<FullMemento> fullSnapshots = new ArrayList<>();
        fullSnapshots.add(new FullMemento(largeCode));

        // DELTA YAKLAŞIMI
        DeltaMemento deltaMemento = new DeltaMemento();
        deltaMemento.setBase(largeCode);

        System.out.println("\n📝 10 Küçük Değişiklik Yapılıyor...");
        System.out.println("   (Her commit'te sadece bir yorum satırı ekleniyor)\n");

        // 10 küçük değişiklik simüle et
        String currentCode = largeCode;
        for (int i = 1; i <= 10; i++) {
            // Küçük bir değişiklik yap (sadece 1 satır yorum)
            String smallChange = "// Change " + i + " added\n";
            currentCode = currentCode + smallChange;

            // Full snapshot kaydet
            fullSnapshots.add(new FullMemento(currentCode));

            // Delta kaydet
            deltaMemento.addDelta(smallChange);

            System.out.println("Commit " + i + ": +" + smallChange.length() + " byte eklendi");
        }

        // Memory karşılaştırması
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 MEMORY KULLANIM KARŞILAŞTIRMASI");
        System.out.println("=".repeat(70));

        // Full snapshot memory
        int fullMemory = 0;
        for (FullMemento m : fullSnapshots) {
            fullMemory += m.size();
        }

        // Delta memory
        int deltaMemory = deltaMemento.size();

        System.out.println("\n🔹 Full Snapshot Yaklaşımı (Klasik Memento):");
        System.out.println("   Snapshot sayısı: " + fullSnapshots.size());
        System.out.println("   Her biri ortalama: ~" + (fullMemory/fullSnapshots.size()/1024) + " KB");
        System.out.println("   TOPLAM: " + fullMemory + " byte (" + (fullMemory/1024) + " KB)");

        System.out.println("\n🔹 Delta (Incremental) Yaklaşımı (Git-like):");
        System.out.println("   Base: " + largeCode.length() + " byte");
        System.out.println("   Delta sayısı: 10");
        System.out.println("   TOPLAM: " + deltaMemory + " byte (" + (deltaMemory/1024) + " KB)");

        // Tasarruf hesapla
        double savings = ((double)(fullMemory - deltaMemory) / fullMemory) * 100;
        int savedKB = (fullMemory - deltaMemory) / 1024;

        System.out.println("\n" + "=".repeat(70));
        System.out.println("💡 SONUÇ:");
        System.out.println("=".repeat(70));
        System.out.println("✅ Delta yaklaşımı %" + String.format("%.1f", savings) + " daha az bellek kullanıyor!");
        System.out.println("✅ Tasarruf: " + savedKB + " KB");
        System.out.println("✅ Bu, " + fullSnapshots.size() + " snapshot yerine sadece 1 base + 10 delta ile başarıldı!");

        System.out.println("\n🎯 SONUÇ: Büyük dosyalarda delta yaklaşımı ÇOK daha verimli!");

        System.out.println("\n" + "=".repeat(70));
        System.out.println("📚 GİT'İN KULLANDIĞI YÖNTEMLERİN EŞDEĞERİ:");
        System.out.println("=".repeat(70));
        System.out.println("1. Delta Compression (Bu örnekte gösterildi)");
        System.out.println("2. Pack Files (Birden fazla delta'yı tek dosyada birleştirme)");
        System.out.println("3. Garbage Collection (Eski, gereksiz snapshot'ları temizleme)");
        System.out.println("4. Adaptive Storage (Küçük değişiklik = delta, büyük değişiklik = full)");

        System.out.println("\n💡 Memento Pattern ile Git arasındaki fark:");
        System.out.println("   Memento: In-memory (RAM'de) undo/redo için");
        System.out.println("   Git: Disk'te kalıcı version control için");
        System.out.println("   İkisi de aynı prensipleri kullanır! (snapshot + delta)");
    }
}

public class RealisticMementoComparison {
    public static void main(String[] args) {
        RealisticScenario.main(args);
    }
}
